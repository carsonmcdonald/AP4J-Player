package net.ioncannon.ap4j.service;

import net.ioncannon.ap4j.model.Device;

import javax.jmdns.ServiceListener;
import javax.jmdns.ServiceEvent;
import javax.jmdns.ServiceInfo;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Copyright (c) 2011 Carson McDonald
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

public class AirPlayJmDNSServiceListener implements ServiceListener
{
  private static Logger logger = Logger.getLogger(AirPlayJmDNSServiceListener.class.getName());

  public AirPlayJmDNSServiceListener()
  {
    logger.log(Level.INFO, "AirPlay listener started.");
  }

  public void serviceAdded(ServiceEvent serviceEvent)
  {
    logger.log(Level.INFO, "Service added: " + serviceEvent);

    ServiceInfo serviceInfo = serviceEvent.getInfo();
    if(serviceInfo == null || serviceInfo.getInetAddress() == null)
    {
      serviceInfo = serviceEvent.getDNS().getServiceInfo(serviceEvent.getType(), serviceEvent.getName(), 2000);
    }
    JettyCommandService.addDevice(new Device(serviceEvent.getName(), serviceInfo.getInetAddress(), serviceInfo.getPort()));
  }

  public void serviceRemoved(ServiceEvent serviceEvent)
  {
    logger.log(Level.INFO, "Service removed: " + serviceEvent);
    
    ServiceInfo serviceInfo = serviceEvent.getInfo();
    if(serviceInfo == null || serviceInfo.getInetAddress() == null)
    {
      serviceInfo = serviceEvent.getDNS().getServiceInfo(serviceEvent.getType(), serviceEvent.getName(), 2000);
    }
    Device device = new Device(serviceEvent.getName(), serviceInfo.getInetAddress(), serviceInfo.getPort());
    JettyCommandService.removeDevice(device.getId());
  }

  public void serviceResolved(ServiceEvent serviceEvent)
  {
    logger.log(Level.INFO, "Service resolved...");
  }
}
