/*
  JWildfire - an image and animation processor written in Java 
  Copyright (C) 1995-2014 Andreas Maschke

  This is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser 
  General Public License as published by the Free Software Foundation; either version 2.1 of the 
  License, or (at your option) any later version.
 
  This software is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without 
  even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU 
  Lesser General Public License for more details.

  You should have received a copy of the GNU Lesser General Public License along with this software; 
  if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
  02110-1301 USA, or see the FSF site: http://www.fsf.org.
*/
package org.jwildfire.create.tina.swing;

import javax.swing.JPanel;

import org.jwildfire.create.tina.render.AbstractRenderThread;
import org.jwildfire.image.SimpleImage;

public class BufferedInteractiveRendererDisplayUpdater implements InteractiveRendererDisplayUpdater {
  private long sampleCount;
  private final JPanel imageRootPanel;
  private final SimpleImage image;

  private final int imageWidth;
  private final int imageHeight;
  private int[] buffer;

  private boolean showPreview;

  public BufferedInteractiveRendererDisplayUpdater(JPanel pImageRootPanel, SimpleImage pImage, boolean pShowPreview) {
    imageRootPanel = pImageRootPanel;
    image = pImage;
    imageWidth = image.getImageWidth();
    imageHeight = image.getImageHeight();
    buffer = getBufferFromImage();
    showPreview = pShowPreview;
  }

  private int[] getBufferFromImage() {
    return image.getBufferedImg().getRGB(0, 0, imageWidth, imageHeight, null, 0, imageWidth);
  }

  @Override
  public void iterationFinished(AbstractRenderThread pEventSource, int pX, int pY) {
    sampleCount++;
    if (showPreview && pX >= 0 && pX < imageWidth && pY >= 0 && pY < imageHeight) {
      int argb = pEventSource.getTonemapper().tonemapSample(pX, pY);
      int offset = imageWidth * pY + pX;
      buffer[offset] = argb;
    }
  }

  @Override
  public void updateImage() {
    if (showPreview) {
      image.getBufferedImg().setRGB(0, 0, imageWidth, imageHeight, buffer, 0, imageWidth);
      imageRootPanel.repaint();
    }
  }

  @Override
  public long getSampleCount() {
    return sampleCount;
  }

  @Override
  public void setSampleCount(long pSampleCount) {
    sampleCount = pSampleCount;
  }

  @Override
  public void setShowPreview(boolean pShowPreview) {
    showPreview = pShowPreview;
  }

}
