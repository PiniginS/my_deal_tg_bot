package ru.kithome.deal_bot.entity

import javax.persistence.*

@Entity
@Table (name = "DEALS")
class DealEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id : Int = 0

    @Column
    var tag : String? = null

    @Column
    var isActive : Boolean = false

    @Column
    var timestamp : String? = null

    @Column
    var description : String? = null
}