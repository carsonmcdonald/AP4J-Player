package net.ioncannon.ap4j.model;

import java.security.MessageDigest;
import java.math.BigInteger;
import java.net.InetAddress;
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

public class Device
{
  private static Logger logger = Logger.getLogger(Device.class.getName());

  private String id;
  private String name;
  private InetAddress inetAddress;
  private int port;

  public Device(String name, InetAddress inetAddress, int port)
  {
    this.name = name;
    this.inetAddress = inetAddress;
    this.port = port;

    try
    {
      MessageDigest messageDigest = MessageDigest.getInstance("MD5");
      messageDigest.update(inetAddress.getAddress());
      messageDigest.update(String.valueOf(port).getBytes("UTF-8"));
      id = String.format("%032X", new BigInteger(1, messageDigest.digest()));
    }
    catch (Exception e)
    {
      logger.log(Level.SEVERE, e.getMessage(), e);
      throw new RuntimeException(e.getMessage(), e);
    }
  }

  public String getName()
  {
    return name;
  }

  public InetAddress getInetAddress()
  {
    return inetAddress;
  }

  public int getPort()
  {
    return port;
  }

  public String getId()
  {
    return id;
  }

  @Override
  public String toString()
  {
    return "Device{" +
        "id='" + getId() + '\'' +
        ", name='" + name + '\'' +
        ", inetAddress='" + inetAddress + '\'' +
        ", port=" + port +
        '}';
  }
}
