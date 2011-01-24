package net.ioncannon.ap4j.model;

import net.ioncannon.ap4j.command.DeviceCommand;

import java.net.Socket;
import java.io.*;
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
 *
 * This class manages the connection to an AirPlay device.
 *
 */
public class DeviceConnection
{
  private static Logger logger = Logger.getLogger(DeviceConnection.class.getName());

  private Device device;
  private Socket socket;

  public DeviceConnection(Device device)
  {
    this.device = device;
  }

  public void close()
  {
    if(socket != null)
    {
      try
      {
        socket.close();
      }
      catch (IOException e)
      {
        // ignore anything here
      }
    }
  }

  public DeviceResponse sendCommand(DeviceCommand command)
  {
    if(socket == null || socket.isClosed())
    {
      try
      {
        socket = new Socket(device.getInetAddress(), device.getPort());
      }
      catch (IOException e)
      {
        logger.log(Level.SEVERE, e.getMessage(), e);
        throw new RuntimeException(e.getMessage(), e);
      }
    }

    try
    {
      BufferedReader deviceInput = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      BufferedWriter deviceOutput = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

      String commandString = command.getCommandString();
      deviceOutput.write(commandString + "\n");
      deviceOutput.flush();

      StringBuffer fullResponse = new StringBuffer();
      String partialResponse;
      while(!(partialResponse = deviceInput.readLine().trim()).equals(""))
      {
        fullResponse.append(partialResponse);
        fullResponse.append("\n");
      }

      int contentLength = 0;
      if(fullResponse.indexOf("Content-Length:") != -1)
      {
        String cls = "Content-Length:";
        int si = fullResponse.indexOf(cls);
        int ei = fullResponse.indexOf("\n", si + cls.length());
        contentLength = Integer.parseInt(fullResponse.substring(si + cls.length(), ei).trim());
      }

      StringBuffer content = null;
      if(contentLength > 0)
      {
        content = new StringBuffer(contentLength);
        char buffer[] = new char[1024];
        int read, totalRead = 0;
        do
        {
          read = deviceInput.read(buffer);
          totalRead += read;
          content.append(buffer, 0, read);
        } while(read != -1 && totalRead < contentLength);
      }

      return new DeviceResponse(fullResponse.toString(), content == null ? null : content.toString());
    }
    catch (IOException e)
    {
      logger.log(Level.SEVERE, e.getMessage(), e);
      throw new RuntimeException(e.getMessage(), e);
    }
  }
}
