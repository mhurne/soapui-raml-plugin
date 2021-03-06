## soapui-raml-plugin

- Allows you to import RAML files into SoapUI for testing your REST APIs
- Allows you to generate a REST Mock Service for a RAML file being imported
- Allows you to udpate an existing REST Service in SoapUI from a RAML file
- Allows you to generate a RAML file for any REST API defined in SoapUI
- Allows you to browse the ApiHub directory for APIs with either RAML or Swagger definitions (Swagger requires the
  [soapui-swagger-plugin](https://github.com/olensmar/soapui-swagger-plugin) to be installed also)

-> See the [blog-post](http://olensmar.blogspot.se/2013/12/a-raml-apihub-plugin-for-soapui.html) for a detailed overview.

### Download & Install

The plugins are available at [sourceforge](https://sourceforge.net/projects/soapui-plugins/files/) -
download and unzip them into the SoapUI/bin folder, this will place files in the underlying folders as follows
(be sure to remove any previous versions of these files):

```
/soapui
   /bin
      /ext
         snakeyaml-1.13.jar (from the raml plugin)
         raml-parser-0.9-SNAPSHOT.jar (from the raml plugin)
         swagger4j-1.0-beta3.jar  (from the swagger plugin)
         Javax.json.1.0-b06.jar  (from the swagger plugin)
      /plugins
         soapui-raml-plugin-0.4-plugin.jar  (from the raml plugin)
         soapui-swagger-plugin-0.3-plugin.jar (from the swagger plugin)
```

(Re)Start SoapUI and create an empty project, you should have the following menu options on the project-popup menu:
- Import RAML Definition; prompts to import a RAML file
- Add API from ApiHub; allows you to browse APIs at ApiHub and import them into SoapUI.

Note: the 0.3 version of the plugin requires SoapUI 5.0 or later (for the REST Mock generation)

### Build it yourself

Clone the Git repository, make sure you have maven installed, and run

```
mvn clean install assembly:single
```

to get the same zip as found on [sourceforge](https://sourceforge.net/projects/soapui-plugins/files/soapui-raml-plugin/)

### Features and shortcomings

The RAML importer supports most constructs in the [RAML 0.8 specification](http://raml.org/spec.html), including
parameterized traits and resourceTypes, !include statements, child resources, query/header/form/uri parameters,
example requests bodies, etc.

Currently ignored are:
- security declarations - mainly because SoapUI doesn't support OAuth yet
- protocols - not sure how that would be mapped to SoapUI where you can add as many endpoints as needed
- schemas - the SoapUI API doesn't make them easy to add programmatically, and it only supports XML Schema for now

I've tested this with a number of RAML files (see src/test/resources and the RamlImporterTests),
but I'm sure there are details I've missed - please let me know if you find anything strange or unexpected.

### Release History

- Version 0.4 - 2014-05-13
  - Added initial "Export RAML" functionality to REST Service popup menu
  - Fixed import of RAML files containing relative includes and multiple request body examples.
  - Added an option do the Import and Update dialogs to enable/disable creation of sample requests.
  - Added separators to popup menus for better readability
- Version 0.3 - 2014-04-22
  - Bugfixes and new "Update from RAML Definition" action which adds new resources/methods/parameters to an existing REST Service in SoapUI
  from the specified RAML file.
  - Added "Generate Mock Service" option when Importing RAML definitions - which creates a SoapUI 5.0 REST Mock using sample response bodies if available.
- Version 0.2 - 2013-11-27
  - Update to use [raml-java-parser](https://github.com/raml-org/raml-java-parser) instead of own raml parser
- Version 0.1 - 2013-11-22 - Initial release

### Feedback is welcome!

Please don't hesitate to comment here or get in touch on twitter; @olensmar

Thanks!

/Ole
