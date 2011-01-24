package net.ioncannon.ap4j.main;

import net.ioncannon.ap4j.service.AirPlayJmDNSServiceListener;

import javax.jmdns.JmDNS;
import java.io.IOException;
import java.util.logging.Logger;
import java.util.logging.Level;

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

public class MDNSRunnable implements Runnable
{
  private static Logger logger = Logger.getLogger(MDNSRunnable.class.getName());

  private JmDNS jmDNS = null;

  public void run()
  {
    try
    {
      jmDNS = JmDNS.create();
      jmDNS.addServiceListener("_airplay._tcp.local.", new AirPlayJmDNSServiceListener());
    }
    catch (Exception e)
    {
      logger.log(Level.SEVERE, e.getMessage(), e);
      Main.shutdown();
    }
  }

  public void shutdown()
  {
    if(jmDNS != null)
    {
      try
      {
        jmDNS.close();
      }
      catch (IOException e)
      {
        logger.log(Level.SEVERE, e.getMessage(), e);
      }
    }
  }
}
