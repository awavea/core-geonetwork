ANZMEST
-------

ANZMEST is GeoNetwork 2.10.x with Australian and New Zealand Metadata Profiles and support.

When you clone ANZMEST select the 2.10.x branch eg:

git clone https://github.com/anzmest/core-geonetwork.git -b 2.10.x --recursive

To build after you have cloned:

```
cd gast
git checkout 2.10.x
git pull
cd ..

cd geoserver
git checkout 2.10.x
git pull
cd ..

cd maven_repo
git checkout master
git pull
cd ..

cd schemaPlugins
git checkout 2.10.x
git pull
cd ..
```

With ANZMEST you get GeoNetwork (2.10.x) plus:

* ISO19115/19139 ANZLIC Profile version 1.1
* ISO19115/19139 Marine Community Profile versions 1.4 and 1.5-experimental
* ANZMETA (the old ANZLIC metadata profile) version 1.3 (view only - no editing)
* ISO19115-3
* Editor from GeoNetwork 3.x
* Nationalmap as map browser

You can view the config overrides that ANZMEST applies to GeoNetwork at:

https://github.com/anzmest/core-geonetwork/blob/2.10.x/web/src/main/webapp/WEB-INF/anzmest-config-overrides.xml

After you have cloned the repository, this file can be found at:

clone-directory-name/web/src/main/webapp/WEB-INF/anzmest-config-overrides.xml

You can review the differences between ANZMEST-2.10.x and GeoNetwork 2.10.x 
in the github interface at https://github.com/anzmest/core-geonetwork/compare/geonetwork:2.10.x...2.10.x

Features
--------

* Immediate search access to local and distributed geospatial catalogues
* Up- and downloading of data, graphics, documents, pdf files and any other content type
* An interactive Web Map Viewer to combine Web Map Services from distributed servers around the world
* Online editing of metadata with a powerful template system
* Scheduled harvesting and synchronization of metadata between distributed catalogs
* Support for OGC-CSW 2.0.2 ISO Profile, OAI-PMH, Z39.50 protocols
* Fine-grained access control with group and user management
* Multi-lingual user interface
