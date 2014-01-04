/*
 * ShinyApplicationSatellite.java
 *
 * Copyright (C) 2009-14 by RStudio, Inc.
 *
 * Unless you have received this program directly from RStudio pursuant
 * to the terms of a commercial license agreement with RStudio, then
 * this program is licensed to you under the terms of version 3 of the
 * GNU Affero General Public License. This program is distributed WITHOUT
 * ANY EXPRESS OR IMPLIED WARRANTY, INCLUDING THOSE OF NON-INFRINGEMENT,
 * MERCHANTABILITY OR FITNESS FOR A PARTICULAR PURPOSE. Please refer to the
 * AGPL (http://www.gnu.org/licenses/agpl-3.0.txt) for more details.
 *
 */
package org.rstudio.studio.client.shiny;

import org.rstudio.studio.client.application.ApplicationUncaughtExceptionHandler;
import org.rstudio.studio.client.application.events.EventBus;
import org.rstudio.studio.client.common.satellite.Satellite;
import org.rstudio.studio.client.common.satellite.SatelliteApplication;
import org.rstudio.studio.client.shiny.events.ShowShinyApplicationEvent;
import org.rstudio.studio.client.shiny.model.ShinyApplicationParams;
import org.rstudio.studio.client.shiny.ui.ShinyApplicationView;
import org.rstudio.studio.client.workbench.views.console.events.SendToConsoleEvent;
import org.rstudio.studio.client.workbench.views.source.editors.text.themes.AceThemes;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

@Singleton
public class ShinyApplicationSatellite extends SatelliteApplication
{
   public static final String NAME = "shiny";
   
   @Inject
   public ShinyApplicationSatellite(ShinyApplicationView view,
                                    Satellite satellite,
                                    Provider<AceThemes> pAceThemes,
                                    ApplicationUncaughtExceptionHandler exHandler,
                                    EventBus eventBus)
   {
      super(NAME, view, satellite, pAceThemes, exHandler);
      eventBus_ = eventBus;
   }

   public void launchShinyApplication(String filePath)
   {
      // TODO: Figure out if the Shiny app satellite window is already
      // showing this app.
      String dir = filePath.substring(0, filePath.lastIndexOf("/"));
      eventBus_.fireEvent(new SendToConsoleEvent(
            "shiny::runApp('" + dir + "')", 
            true));
      
      // TODO: Listen at the console to figure out what random port was assigned,
      // so we can point the viewer at the right place.
      eventBus_.fireEvent(new ShowShinyApplicationEvent(
                          ShinyApplicationParams.create("http://127.0.0.1/")));
   }
   
   private EventBus eventBus_;
}
