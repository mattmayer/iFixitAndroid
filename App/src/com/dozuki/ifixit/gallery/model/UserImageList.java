package com.dozuki.ifixit.gallery.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class UserImageList implements Serializable {
   private static final long serialVersionUID = 7067096480019401662L;

   private ArrayList<UserImageInfo> mImages;

   public UserImageList() {
      mImages = new ArrayList<UserImageInfo>();
   }

   public void addImage(UserImageInfo userImageInfo) {
      if (mImages.contains(userImageInfo))
         return;
      mImages.add(userImageInfo);
      Collections.sort(mImages, new UserImageInfoComparator());
   }

   public ArrayList<UserImageInfo> getImages() {
      return mImages;
   }

   public void setImages(ArrayList<UserImageInfo> images) {
      mImages = images;
   }

   private static class UserImageInfoComparator implements
    Comparator<UserImageInfo> {
      public int compare(UserImageInfo e1, UserImageInfo e2) {
         if (e1.getImageid() == null && e2.getImageid() == null)
            return 0;

         if (e1.getImageid() == null)
            return 1;

         if (e2.getImageid() == null)
            return -1;

         return Integer.parseInt(e1.getImageid()) - Integer.parseInt(
          e2.getImageid());
      }
   }
}
