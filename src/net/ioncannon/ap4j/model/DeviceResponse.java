package net.ioncannon.ap4j.model;

import java.util.HashMap;
import java.util.Map;

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

public class DeviceResponse
{
  private int responseCode;
  private String responseMessage;
  private Map<String, String> headerMap = new HashMap<String, String>();
  private String content;
  private Map<String, String> contentParameterMap = new HashMap<String, String>();

  public DeviceResponse(String headers, String content)
  {
    String headerSplit[] = headers.split("\n");

    String rawResponseValue = headerSplit[0];
    String responseSplit[] = rawResponseValue.split(" ");
    responseCode = Integer.parseInt(responseSplit[1]);
    responseMessage = responseSplit[2];

    for(int i=1; i<headerSplit.length; i++)
    {
      String headerValueSplit[] = headerSplit[i].split(":");
      headerMap.put(headerValueSplit[0], headerValueSplit[1].trim());
    }

    if(content != null)
    {
      this.content = content;

      if("text/parameters".equalsIgnoreCase(headerMap.get("Content-Type")))
      {
        for(String paramLine : content.split("\n"))
        {
          String paramSplit[] = paramLine.split(":");
          contentParameterMap.put(paramSplit[0], paramSplit[1].trim());
        }
      }
    }
  }

  public int getResponseCode()
  {
    return responseCode;
  }

  public String getResponseMessage()
  {
    return responseMessage;
  }

  public Map<String, String> getHeaderMap()
  {
    return headerMap;
  }

  public String getContent()
  {
    return content;
  }

  public boolean hasContentParameters()
  {
    return !contentParameterMap.isEmpty();
  }

  public Map<String, String> getContentParameterMap()
  {
    return contentParameterMap;
  }
}
