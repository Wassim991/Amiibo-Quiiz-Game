package fr.ceri.amiibo

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Amiibo : RealmObject() {
    @PrimaryKey
    var id: String = ""
    var name: String = ""
    var gameSeries: String = ""
    var image: String = ""
}
