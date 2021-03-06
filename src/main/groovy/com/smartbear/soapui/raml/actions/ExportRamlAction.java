package com.smartbear.soapui.raml.actions;

import com.eviware.soapui.impl.rest.RestService;
import com.eviware.soapui.impl.rest.RestServiceFactory;
import com.eviware.soapui.impl.settings.XmlBeansSettingsImpl;
import com.eviware.soapui.impl.wsdl.WsdlInterface;
import com.eviware.soapui.impl.wsdl.WsdlProject;
import com.eviware.soapui.model.support.ModelSupport;
import com.eviware.soapui.support.StringUtils;
import com.eviware.soapui.support.UISupport;
import com.eviware.soapui.support.action.support.AbstractSoapUIAction;
import com.eviware.x.form.XFormDialog;
import com.eviware.x.form.XFormOptionsField;
import com.eviware.x.form.support.ADialogBuilder;
import com.eviware.x.form.support.AField;
import com.eviware.x.form.support.AForm;
import com.smartbear.soapui.raml.RamlExporter;

import java.io.File;
import java.io.FileWriter;

public class ExportRamlAction extends AbstractSoapUIAction<RestService>
{
    private static final String TARGET_PATH = Form.class.getName() + Form.FOLDER;
    private static final String VERSION = Form.class.getName() + Form.VERSION;

    private XFormDialog dialog;

    public ExportRamlAction()
    {
        super( "Export RAML", "Creates a RAML definition for selected REST API" );
    }

    public void perform( RestService restService, Object param )
    {
        // initialize form
        XmlBeansSettingsImpl settings = restService.getSettings();
        if( dialog == null )
        {
            dialog = ADialogBuilder.buildDialog(Form.class);

            String name = restService.getName();
            if( name.startsWith("/") || name.toLowerCase().startsWith("http"))
                name = restService.getProject().getName() + " - " + name;

            dialog.setValue(Form.TITLE, name);
            dialog.setValue(Form.BASEURI, restService.getBasePath());
            dialog.setValue(Form.FOLDER, settings.getString(TARGET_PATH, ""));
            dialog.setValue(Form.VERSION, settings.getString(VERSION, "" ));
        }

        while( dialog.show() )
        {
            try
            {
                RamlExporter exporter = new RamlExporter( restService.getProject() );

                exporter.setCreateSampleBodies( dialog.getBooleanValue( Form.CREATE_SAMPLE_BODIES));
                String raml = exporter.createRaml( dialog.getValue(Form.TITLE), restService,
                        dialog.getValue(Form.BASEURI), dialog.getValue(Form.VERSION));


                String folder = dialog.getValue( Form.FOLDER );

                File file = new File( exporter.createFileName( folder, dialog.getValue(Form.TITLE)));
                FileWriter writer = new FileWriter( file );
                writer.write( raml );
                writer.close();

                UISupport.showInfoMessage("RAML definition has been created at [" + file.getAbsolutePath() + "]");

                settings.setString(TARGET_PATH, dialog.getValue(Form.FOLDER));

                break;
            }
            catch( Exception ex )
            {
                UISupport.showErrorMessage( ex );
            }
        }
    }

    @AForm( name = "Export RAML Definition", description = "Creates a RAML definition for selected REST APIs in this project" )
    public interface Form
    {
        @AField( name = "Target Folder", description = "Where to save the RAML definition", type = AField.AFieldType.FOLDER )
        public final static String FOLDER = "Target Folder";

        @AField( name = "Title", description = "The API Title", type = AField.AFieldType.STRING )
        public final static String TITLE = "Title";

        @AField( name = "Base URI", description = "The RAML baseUri", type = AField.AFieldType.STRING )
        public final static String BASEURI = "Base URI";

        @AField( name = "Version", description = "The default version if a {version} uri parameter is in the baseUri", type = AField.AFieldType.STRING )
        public final static String VERSION = "Version";

        @AField(name = "Create Sample Bodies", description = "Create sample request/response bodies from existing requests", type = AField.AFieldType.BOOLEAN)
        public final static String CREATE_SAMPLE_BODIES = "Create Sample Bodies";

    }
}
