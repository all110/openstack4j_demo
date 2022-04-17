package com.chinaums.ysmktaln.spbillserv.common.interfaces.impl;

import com.chinaums.ysmktaln.spbillserv.common.interfaces.CloudManager;
import com.chinaums.ysmktaln.spbillserv.common.model.CloudProperties;
import java.util.List;
import org.openstack4j.model.compute.Image;
import org.openstack4j.model.compute.Server;
import org.openstack4j.model.storage.block.Volume;
import org.openstack4j.model.storage.block.VolumeSnapshot;

public class PublicCloudManagerImpl implements CloudManager {
   public PublicCloudManagerImpl(CloudProperties publicCloudProperties) {
   }

   public String createServerFromImage(String serverName, String flavorId, String imageId) {
      return null;
   }

   public Server showServer(String serverId) {
      return null;
   }

   public Volume createBootableVolume(String volumeName, String volumeSnapshotId) {
      return null;
   }

   public Server createServerFormVolume(String serverName, String flavorId, String volumeId) {
      return null;
   }

   public void deleteServer(String serverId) {
   }

   public void deleteVolume(String volumeId) {
   }

   public Volume showVolume(String volumeId) {
      return null;
   }

   public List<? extends Volume> listVolumes() {
      return null;
   }

   public VolumeSnapshot getVolumeSnapshot(String volumeSnapshotId) {
      return null;
   }

   public List<? extends VolumeSnapshot> listVolumeSnapshots() {
      return null;
   }

   public List<? extends Server> listServers() {
      return null;
   }

   public void rebootServer(String serverId) {
   }

   public void shutOffServer(String serverId) {
   }

   public void startServer(String serverId) {
   }

   public VolumeSnapshot createSnapshot(String serverId, String snapshotName) {
      return null;
   }

   public String showSnapshotStatus(String snapshotId) {
      return null;
   }

   public Image getImageByimageId(String imageId) {
      return null;
   }

   public List<? extends Image> listImages() {
      return null;
   }

   public void deleteVolumeSnapshot(String snapshotId) {
   }

   public VolumeSnapshot getVolumeSnapshotByimageId(String imageId) {
      return null;
   }
}
