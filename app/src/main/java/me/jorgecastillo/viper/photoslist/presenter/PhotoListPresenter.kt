package me.jorgecastillo.viper.photoslist.presenter

import arrow.core.Either
import me.jorgecastillo.viper.common.domain.error.Error
import me.jorgecastillo.viper.common.presenter.BasePresenter
import me.jorgecastillo.viper.photoslist.domain.interactor.GetPhotos
import me.jorgecastillo.viper.photoslist.domain.model.Photo

class PhotoListPresenter(private val getPhotos: GetPhotos) :
    BasePresenter<PhotoListPresenter.View>() {

  interface View : BasePresenter.View {
    fun renderPhotos(photos: List<Photo>)
    fun displayLoadingPhotosError()
  }

  init {
    loadPhotos()
  }

  private fun loadPhotos() {
    val params = GetPhotos.Params(page = 1, query = "landscape")
    getPhotos.execute(params, ::onPhotosArrived)
  }

  private fun onPhotosArrived(result: Either<Error, List<Photo>>) {
    result.fold(ifLeft = {
      view?.displayLoadingPhotosError()
    }, ifRight = {
      view?.renderPhotos(it)
    })
  }

  fun onAddButtonClicked() {

  }
}