package com.example.mb2

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cell_info_table")
class CellInfo(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    val type: String,
    val lte_rxlev: String,
    val gsm_rssi: String,
    val umts_rscp: String,
    val lte_rsrp: String,
    val lte_rsrq: String,
    val lte_cqi: String,
    val strength: String,
    val arfcn: String,
    val lac: String,
    val tac: String,
    val cid: String,
    val mcc : String,
    val mnc : String,
    val plmn: String,
    val longitude: Double,
    val altitude: Double,
    val time: Long,
    val latency: Long,
    val content_latency: Long,
    val jitter: Int,
    val upSpeed: Int,
    val downSpeed: Int
)