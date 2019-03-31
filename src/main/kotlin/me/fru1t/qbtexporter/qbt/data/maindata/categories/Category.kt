package me.fru1t.qbtexporter.qbt.data.maindata.categories

import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Since
import me.fru1t.qbtexporter.qbt.QbtVersion

/** A single qBittorrent category of which a single torrent can only be assigned up to one of. */
data class Category(
  /** The user-defined name for this category. */
  @Since(QbtVersion.RELEASE_4_1_5)
  @SerializedName("name")
  val name: String?,

  /**
   * The save path for this category. When automatic torrent management is enabled for a given
   * torrent, that torrent will be placed in this path.
   */
  @Since(QbtVersion.RELEASE_4_1_5)
  @SerializedName("savePath")
  val savePath: String?
)
