//=============================================================================
//===	Copyright (C) 2001-2012 Food and Agriculture Organization of the
//===	United Nations (FAO-UN), United Nations World Food Programme (WFP)
//===	and United Nations Environment Programme (UNEP)
//===
//===	This program is free software; you can redistribute it and/or modify
//===	it under the terms of the GNU General Public License as published by
//===	the Free Software Foundation; either version 2 of the License, or (at
//===	your option) any later version.
//===
//===	This program is distributed in the hope that it will be useful, but
//===	WITHOUT ANY WARRANTY; without even the implied warranty of
//===	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
//===	General Public License for more details.
//===
//===	You should have received a copy of the GNU General Public License
//===	along with this program; if not, write to the Free Software
//===	Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301, USA
//===
//===	Contact: Jeroen Ticheler - FAO - Viale delle Terme di Caracalla 2,
//===	Rome - Italy. email: geonetwork@osgeo.org
//==============================================================================

package org.fao.geonet.services.metadata;

import jeeves.constants.Jeeves;
import jeeves.interfaces.Service;
import jeeves.resources.dbms.Dbms;
import jeeves.server.ServiceConfig;
import jeeves.server.context.ServiceContext;
import jeeves.utils.Util;
import jeeves.utils.Xml;
import org.fao.geonet.GeonetContext;
import org.fao.geonet.constants.Geonet;
import org.fao.geonet.constants.Params;
import org.fao.geonet.kernel.DataManager;
import org.fao.geonet.kernel.MdInfo;
import org.fao.geonet.services.NotInReadOnlyModeService;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;

import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Extracts fragment(s) from a metadata record specified by uuid/id. 
 */
public class ExtractFragment extends NotInReadOnlyModeService {
	private Map<String,List> namespaceList = new HashMap<String,List>();

	public void init(String appPath, ServiceConfig params) throws Exception {}

	//--------------------------------------------------------------------------
	//---
	//--- Service
	//---
	//--------------------------------------------------------------------------

	public Element serviceSpecificExec(Element params, ServiceContext context) throws Exception
	{
		GeonetContext gc = (GeonetContext) context.getHandlerContext(Geonet.CONTEXT_NAME);

		Dbms dbms = (Dbms) context.getResourceManager().open(Geonet.Res.MAIN_DB);

		String uuid = Util.getParam(params, Params.UUID);
		String xpath = Util.getParam(params, Params.XPATH);

		Element response = new Element(Jeeves.Elem.RESPONSE);
		processRecord(context, dbms, uuid, xpath, response);
		return response; 
	}

	//--------------------------------------------------------------------------
	//---
	//--- Private methods
	//---
	//--------------------------------------------------------------------------

	private void processRecord(ServiceContext context, Dbms dbms, String uuid, String xpath, Element response) throws Exception {

		GeonetContext gc = (GeonetContext) context.getHandlerContext(Geonet.CONTEXT_NAME);
		DataManager   dataMan   = gc.getDataManager();

		if (context.isDebug())
			context.debug("Extracting fragment from metadata with uuid:"+uuid+" with xpath: "+xpath);

		String id   = dataMan.getMetadataId(dbms, uuid);

		// Metadata may have been deleted since selection
		if (id != null) {
	
			MdInfo info = dataMan.getMetadataInfo(dbms, id);
	
			if (info == null) {
				throw new Exception("Metadata "+uuid+" not found");
			} else {
				extractFragment(context, dbms, dataMan, xpath, id, response); 	
			}
		} else {
      throw new Exception("Metadata "+uuid+" not found");
		}
	}

	private void extractFragment(ServiceContext context, Dbms dbms, DataManager dataMan, String xpath, String id, Element response) throws Exception {

		// get metadata
		Element md = dataMan.getMetadataNoInfo(context, id);
		MdInfo mdInfo = dataMan.getMetadataInfo(dbms, id);

		// Build a list of all Namespaces in the metadata document
		List metadataNamespaces = namespaceList.get(mdInfo.schemaId);
		if (metadataNamespaces == null) {
			metadataNamespaces = new ArrayList();
			Namespace ns = md.getNamespace();
			if (ns != null) {
				metadataNamespaces.add(ns);
				metadataNamespaces.addAll(md.getAdditionalNamespaces());
				namespaceList.put(mdInfo.schemaId, metadataNamespaces);
			}
		}

		new Document(md);
		// select all nodes that come back from the xpath selectNodes
		List nodes = Xml.selectNodes(md, xpath, metadataNamespaces);
		if (context.isDebug()) {
			context.debug("xpath \n"+xpath+"\n returned "+nodes.size()+" results");
		}
		response.setAttribute("matches", nodes.size()+"");

		// add each node to response
		for (Iterator iter = nodes.iterator(); iter.hasNext();) {
			Object o = iter.next();
			if (o instanceof Element) {
				Element elem = (Element)o;
        response.addContent((Element)elem.clone());
      }
		}

	}

}
