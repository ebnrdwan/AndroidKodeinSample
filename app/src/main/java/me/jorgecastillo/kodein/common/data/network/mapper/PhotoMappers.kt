package me.jorgecastillo.kodein.common.data.network.mapper

import me.jorgecastillo.kodein.common.data.network.model.PhotoDto
import me.jorgecastillo.kodein.common.data.network.model.author
import me.jorgecastillo.kodein.common.domain.model.Photo

fun List<PhotoDto>.toDomain(): List<Photo> = this.map { it.toDomain() }

fun PhotoDto.toDomain(): Photo = Photo(
    this.id,
    this.urls.regular,
    this.author,
    this.description,
    this.created_at)
