package com.chinaums.ysmktaln.spbillserv.common.interfaces;

import java.util.List;
import org.openstack4j.model.compute.Image;
import org.openstack4j.model.compute.Server;
import org.openstack4j.model.storage.block.Volume;
import org.openstack4j.model.storage.block.VolumeSnapshot;

public interface CloudManager {
   String createServerFromImage(String serverName, String flavorId, String imageId);

   Server showServer(String serverId);

   Volume createBootableVolume(String volumeName, String volumeSnapshotId);

   Server createServerFormVolume(String serverName, String flavorId, String volumeId);

   void deleteServer(String serverId);

   void deleteVolume(String volumeId);

   Volume showVolume(String volumeId);

   List<? extends Volume> listVolumes();

   VolumeSnapshot getVolumeSnapshot(String volumeSnapshotId);

   List<? extends VolumeSnapshot> listVolumeSnapshots();

   List<? extends Server> listServers();

   void rebootServer(String serverId);

   void shutOffServer(String serverId);

   void startServer(String serverId);

   VolumeSnapshot createSnapshot(String serverId, String snapshotName);

   String showSnapshotStatus(String snapshotId);

   Image getImageByimageId(String imageId);

   List<? extends Image> listImages();

   void deleteVolumeSnapshot(String snapshotId);

   VolumeSnapshot getVolumeSnapshotByimageId(String imageId);
}
