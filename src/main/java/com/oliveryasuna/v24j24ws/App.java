package com.oliveryasuna.v24j24ws;

import com.vaadin.flow.server.VaadinServlet;
import com.vaadin.flow.server.startup.ServletContextListeners;
import org.eclipse.jetty.ee10.annotations.AnnotationConfiguration;
import org.eclipse.jetty.ee10.plus.webapp.EnvConfiguration;
import org.eclipse.jetty.ee10.plus.webapp.PlusConfiguration;
import org.eclipse.jetty.ee10.servlet.ServletHolder;
import org.eclipse.jetty.ee10.webapp.*;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.util.resource.ResourceFactory;

public final class App {

  // Entry point
  //--------------------------------------------------

  public static void main(final String[] args) throws Exception {
    final Server server = createServer();

    server.start();
    server.join();
  }

  // Static methods
  //--------------------------------------------------

  private static Server createServer() {
    final Server server = new Server(8080);
    final WebAppContext context = createContext();

    server.setHandler(context);

    return server;
  }

  private static WebAppContext createContext() {
    final WebAppContext context = new WebAppContext();

    final ResourceFactory resourceFactory = ResourceFactory.of(context);

    context.setBaseResource(resourceFactory.newResource(App.class.getResource("/webapp")));
    context.setContextPath("/");

    final ServletHolder servletHolder = createServlet(context);

    servletHolder.setInitOrder(1);

    context.setAttribute(MetaInfConfiguration.CONTAINER_JAR_PATTERN, ".*");
    context.setConfigurationDiscovered(true);
    context.setParentLoaderPriority(true);
    context.setConfigurations(new Configuration[] {
        new AnnotationConfiguration(),
        new WebAppConfiguration(),
        new WebInfConfiguration(),
        new WebXmlConfiguration(),
        new MetaInfConfiguration(),
        new FragmentConfiguration(),
        new EnvConfiguration(),
        new PlusConfiguration(),
        new JettyWebXmlConfiguration()
    });
    context.addEventListener(new ServletContextListeners());

    return context;
  }

  private static ServletHolder createServlet(final WebAppContext context) {
    return context.addServlet(VaadinServlet.class, "/*");
  }

}
