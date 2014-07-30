package cl.ps.asistencia.ui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.teemu.wizards.Wizard;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

public class CSV extends Panel implements View {
	
	Logger logger = LoggerFactory.getLogger(CSV.class);
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 1932184562292323699L;
	
	public static final String NAME = "ui_carga_csv";
    
	public CSV(){
		
		//utiliza todo el espacio
		addStyleName("dashboard-view");
		setSizeFull();
		
		VerticalLayout root = new VerticalLayout();
        root.setSizeFull();
        root.setSpacing(true);
        setContent(root);
        
      //agrega la parte de arriba con el título y la información
	    HorizontalLayout top = drawTop();
	    top.setWidth("95%");
	    root.addComponent(top);
	    
	    CssLayout contenido = drawContenido();
	    root.addComponent(contenido);
	    root.setExpandRatio(contenido, 1.0F);
	}

	private CssLayout drawContenido() {

		
		CssLayout contenido = new CssLayout();
		contenido.setSizeFull();
		contenido.addStyleName("layout-panel");
		
		Wizard wizard = new Wizard();
		
		wizard.addStep(new SubirArchivo());
		wizard.addStep(new AgregarParametros());
		wizard.addStep(new GenerarExcel());
		wizard.addStep(new TransformarExcel());
	   	   
	    contenido.addComponent(wizard);
	    
	    return contenido;
	}

	private HorizontalLayout drawTop() {

		HorizontalLayout top = new HorizontalLayout();
        top.setWidth("100%");
        top.setSpacing(true);
        top.setMargin(false);
        
        Label title = new Label(
        		"<span class='v-label h1' >Carga de archivo CSV</span><br />",
        		ContentMode.HTML);
        
        title.setSizeUndefined();
        top.addComponent(title);
        top.setComponentAlignment(title, Alignment.MIDDLE_LEFT);
        
        return top;
	    
	}

	@Override
	public void enter(ViewChangeEvent event) {
		
	}
	


}
