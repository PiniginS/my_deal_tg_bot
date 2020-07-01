package ru.kithome.deal_bot.entity

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "DEALS")
class DealEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int = 0

    @Column
    var tag: String? = null

    @Column
    var isActive: Boolean = false

    @Column
    var timestamp: String? = null

    @Column
    var description: String? = null
}
