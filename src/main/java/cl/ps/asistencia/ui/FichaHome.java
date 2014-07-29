package cl.ps.asistencia.ui;

import com.vaadin.annotations.Theme;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@Theme("dashboard")
public class FichaHome extends Panel implements View {

	/**
	 * 
	 */
	private static final long serialVersionUID = 16569948508628877L;

    public static final String NAME = "ui_hoja_de_vida";
	
	public FichaHome(){
		
		//utiliza todo el espacio
		setSizeFull();
		addStyleName("dashboard-view");
		
		VerticalLayout root = new VerticalLayout();
        root.setSizeFull();
        root.setSpacing(true);
        setContent(root);
        
      //agrega la parte de arriba con el título y la información
	    HorizontalLayout top = drawTop();
	    top.setWidth("95%");
	    root.addComponent(top);
	    
	    VerticalLayout vl = new VerticalLayout();
	    vl.setSizeFull();
		
	    CssLayout filtro = drawFiltro();
		
		CssLayout vsl = drawContent(); 
		
	    vl.addComponent(filtro);
	    vl.addComponent(vsl);
	    vl.setExpandRatio(vsl, 1.0F);
	    
	    root.addComponent(vl);
	    root.setExpandRatio(vl, 1.0F);
		
	}
	
	private HorizontalLayout drawTop() {

		HorizontalLayout top = new HorizontalLayout();
        top.setWidth("100%");
        top.setSpacing(true);
        top.setMargin(false);
        
        Label title = new Label(
        		"<span class='v-label h1' >Hojas de vida</span><br />",
        		ContentMode.HTML);
        
        title.setSizeUndefined();
        top.addComponent(title);
        top.setComponentAlignment(title, Alignment.MIDDLE_LEFT);
        
        return top;
	    
	}
	

	private CssLayout drawContent() {
		
		CssLayout contenido = new CssLayout();
		contenido.setSizeFull();
		contenido.addStyleName("layout-panel");
		
		HorizontalSplitPanel hsp = new HorizontalSplitPanel();
		contenido.addComponent(hsp);
		hsp.setSplitPosition(15);
		
		hsp.setFirstComponent(
				new VerticalLayout(){{
					setWidth("100%");
					addStyleName("sidebar2");
					addComponent(
							new CssLayout(){{
								addStyleName("menu2"); 
								int i = 0;
								for(String s : new String[]{ "Jose perez" ,"Jorge Saavedra", "Patricio Torres", "Carlos Cornejo"} ){
									Button b = new NativeButton(s);
									if(i == 0)
										b.setIcon(FontAwesome.SMILE_O);
									else if(i == 1)
										b.setIcon(FontAwesome.FROWN_O);
									else
										b.setIcon(FontAwesome.MEH_O);
									addComponent(b);
									i++;
								}
						}});
								
				}});
		
		hsp.setSecondComponent(new CssLayout(){{
			HorizontalLayout top = new HorizontalLayout();
			top.setSpacing(true);
			addComponent(top);
			
			// A theme resource in the current theme ("mytheme")
			// Located in: VAADIN/themes/mytheme/img/themeimage.png
			ThemeResource resource = new ThemeResource("img/915.JPG");
			Image img = new Image(null, resource);
			img.setWidth("140px");
			img.setHeight("160px");
			top.addComponent(img);
			
			VerticalLayout vl = new VerticalLayout();
			top.addComponent(vl);
			
			vl.addComponent(new Label("<h1>Jose Perez</h1>",ContentMode.HTML));
			vl.addComponent(new Label("<h3>111111-1</h3>",ContentMode.HTML));
			
			
		}});

		return contenido;
	}


	private CssLayout drawFiltro() {
		
		CssLayout contenido = new CssLayout();
		contenido.setHeight("140px");
		contenido.addStyleName("layout-panel");
		
		HorizontalLayout hl = new HorizontalLayout();
		contenido.addComponent(hl);
		hl.setWidth("100%");
		hl.setSpacing(false);
		
		HorizontalLayout hl2 = new HorizontalLayout();
		hl2.setSpacing(true);
		TextField nombre = new TextField("Nombre");
		hl2.addComponent(nombre);
		hl2.setComponentAlignment(nombre, Alignment.BOTTOM_LEFT);
		
		Button btn = new Button(null,FontAwesome.SEARCH );
		hl2.addComponent(btn);
		hl2.setComponentAlignment(btn, Alignment.BOTTOM_LEFT);
		
		hl.addComponent(hl2);
		
		ComboBox cb = new ComboBox("Estado");
		cb.addItem("Vigente");
		cb.addItem("No Vigente");
		cb.setNullSelectionAllowed(false);
		cb.select("Vigente");
		hl2.addComponent(cb);
		
		ComboBox cb2 = new ComboBox("Faena");
		cb2.addItem("Faena1");
		cb2.addItem("Faena2");
		cb2.addItem("Faena3");
		cb2.setNullSelectionAllowed(false);
		cb2.select("Faena1");
		hl2.addComponent(cb2);
		
		return contenido;
	}


	@Override
	public void enter(ViewChangeEvent event) {

	}

}
