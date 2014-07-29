package cl.ps.asistencia;

import java.util.Iterator;

import javax.servlet.annotation.WebServlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cl.ps.asistencia.ui.CSV;
import cl.ps.asistencia.ui.FichaHome;
import cl.ps.asistencia.ui.Login;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinService;
import com.vaadin.server.VaadinServlet;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.DragAndDropWrapper;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.Command;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@Theme("dashboard")
@SuppressWarnings("serial")
public class MyVaadinUI extends UI 
{
	
	Logger logger = LoggerFactory.getLogger(MyVaadinUI.class);

    @WebServlet(value = "/*", asyncSupported = true)
    @VaadinServletConfiguration(productionMode = false, ui = MyVaadinUI.class, widgetset = "cl.ps.asistencia.AppWidgetSet")
    public static class Servlet extends VaadinServlet {
    }
    
    CssLayout root,menu;
    VerticalLayout menuLayout;
	HorizontalLayout bodyLayout;
    Navigator navigator;

    @Override
    protected void init(VaadinRequest request) {
    	
    	root = new CssLayout();
		setContent(root);
        root.addStyleName("root");
        root.setSizeFull();
        
        bodyLayout = new HorizontalLayout();
		bodyLayout.setSizeFull();
		
		CssLayout content = new CssLayout();
		content.setSizeFull();
		content.addStyleName("view-content");
    	
    	//agregando navigator
    	navigator = new Navigator(this,content);
    	
    	menuLayout = drawMenu();

		bodyLayout.addComponent(menuLayout);
		bodyLayout.addComponent(content);
		bodyLayout.setExpandRatio(content, 1);
		
		root.addComponent(bodyLayout);
		
    	// Create and register the views
        navigator.addView(FichaHome.NAME, new FichaHome());
        navigator.addView(CSV.NAME, new CSV());
        navigator.addView(Login.NAME, new Login());
    	if(navigator.getState() == null || navigator.getState().isEmpty())
    		navigator.navigateTo(Login.NAME);
      }
    
    final static ThemeResource resource = new ThemeResource("img/branding.png");

	private VerticalLayout drawMenu() {
		
		VerticalLayout vl = new VerticalLayout();
		vl.addStyleName("sidebar");
		vl.setWidth(null);
		vl.setHeight("100%");
        
     // Branding element
        vl.addComponent(new CssLayout() {
            {
                addStyleName("branding");
                Label logo = new Label(
                  "<span style=\"text-align:center\" ><img style=\"width:50px;\" src=\"" +
                		  VaadinService.getCurrentRequest().getContextPath() 
                       + "/VAADIN/themes/dashboard"
                       + "/" + resource.getResourceId() +
                   "\"/> </span><span>Grupo Magal</span> Dashboard",
                                ContentMode.HTML);
                   logo.setSizeUndefined();
                   addComponent(logo);
               }
        	});
        
		menu = new CssLayout();
		menu.addStyleName("menu");
		
		
		String[] vistas = new String[] {CSV.NAME, FichaHome.NAME };
		
		for (final String view : vistas ) {
			
			logger.debug("view: " + view);
			
			//TODO revisa los permisos
			
			String nombre = view.substring(3);
            Button b = new NativeButton(nombre.substring(0,1).toUpperCase()
                    + nombre.substring(1).replace('-', ' '));
            
            b.setIcon(getViewIcon(view));
            b.addClickListener(new ClickListener() {
                @Override
                public void buttonClick(ClickEvent event) {
                	clearMenuSelection();
                    event.getButton().addStyleName("selected");
                	if (!navigator.getState().equals(view))
                		navigator.navigateTo(view);
                }
            });
            b.setData(view);
            
            menu.addComponent(b);
        }

		vl.addComponent(menu);
		vl.setExpandRatio(menu, 1.0F);

        vl.addComponent(new VerticalLayout() {
        	{
        		setSizeUndefined();
                addStyleName("user");
                Image profilePic = new Image(
                        null,
                        new ThemeResource("img/profile-pic.png"));
                profilePic.setWidth("34px");
                addComponent(profilePic);
                
                String nombreUsuario = "Usuario";
//                if( context != null){
//                	 Boolean usuario = (Boolean) VaadinSession.getCurrent().getAttribute(Constantes.SESSION_USUARIO);
//                     if(usuario != null && usuario ){
////                         
////                         if(usuario.getNombre() != null){
////                         	nombreUsuario = usuario.getNombre();
////                         	if(usuario.getApellido1() != null)
////                         		nombreUsuario+= " " + usuario.getApellido1();
////                         }
//                     }
//                }
               
                
                Label userName = new Label(nombreUsuario);
                userName.setSizeUndefined();
                addComponent(userName);

                Command cmd = new Command() {
                    @Override
                    public void menuSelected(
                            MenuItem selectedItem) {
                        Notification
                                .show("Not implemented in this demo");
                    }
                };
                
                MenuBar settings = new MenuBar();
                MenuItem settingsMenu = settings.addItem("",
                        null);
                settingsMenu.setStyleName("icon-cog");
                settingsMenu.addItem("Settings", cmd);
                settingsMenu.addItem("Preferences", cmd);
                settingsMenu.addSeparator();
                settingsMenu.addItem("My Account", cmd);
                addComponent(settings);
                
		        Button exit = new NativeButton("Salir");
		        exit.addStyleName("icon-cancel");
		        exit.setDescription("Salir");
		        addComponent(exit);
		        exit.addClickListener(new ClickListener() {
		            @Override
		            public void buttonClick(ClickEvent event) {
		            	
		            	logOut();
		            }
		        });
        	}
        });
		
		return vl;
	}
	
	private Resource getViewIcon(String view) {
		if( view.equals(CSV.NAME) )
			return FontAwesome.UPLOAD;
		if( view.equals(FichaHome.NAME) )
			return FontAwesome.BOOK;
		throw new RuntimeException("La vista no tiene icono asociado");
	}
	
	private void clearMenuSelection() {
        for (Iterator<Component> it = menu.getComponentIterator(); it.hasNext();) {
            Component next = it.next();
            if (next instanceof NativeButton) {
                next.removeStyleName("selected");
            } else if (next instanceof DragAndDropWrapper) {
                // Wow, this is ugly (even uglier than the rest of the code)
                ((DragAndDropWrapper) next).iterator().next()
                        .removeStyleName("selected");
            }
        }
    }
	
	private void setMenuSelection(String view) {
        for (Iterator<Component> it = menu.getComponentIterator(); it.hasNext();) {
            Component next = it.next();
            if (next instanceof NativeButton) {
            	Button btn = (NativeButton) next;
            	if ( btn.getData().equals(view))
            		next.addStyleName("selected");
            	else
            		next.removeStyleName("selected");
            } 
        }
    }
	
	public void logOut(){
//		SecurityContext context = (SecurityContext) VaadinSession.getCurrent().getAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY);
//		if( context != null ){
//			
//			Authentication authentication = (Authentication) context.getAuthentication();
//			logoutHandler.logout(VaadinRequestHolder.getRequest(), null , authentication);
//		}
//    	//deslogea
//    	VaadinSession.getCurrent().
//    	setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,SecurityContextHolder.createEmptyContext());
    	navigator.navigateTo(Login.NAME);
	}
	
	public Component getMenuLayout() {
		return menuLayout;
	}
	
	public Component getContentLayout(){
		return bodyLayout;
	}

	public void refreshMenuLayout() {
		logger.debug("REFRESH MENULAYOUT");
//		menuLayout = drawMenu();
		VerticalLayout vl = drawMenu();
		bodyLayout.replaceComponent(menuLayout, vl);
		menuLayout = vl;
		
	}

}
