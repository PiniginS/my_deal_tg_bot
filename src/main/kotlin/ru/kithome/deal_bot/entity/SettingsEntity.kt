package ru.kithome.deal_bot.entity

import javax.persistence.*

@Entity
@Table (name = "SETTINGS")
class SettingsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id : Int = 0

    @Column
    var key : String? = null

    @Column
    var value : String? = null
}