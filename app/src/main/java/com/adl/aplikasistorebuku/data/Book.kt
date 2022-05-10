package com.adl.aplikasistorebuku.data

data class Book(val judul:String, val harga:String, val sipnosis:String, val gambar:String ){
    constructor():this("","","","")
}
