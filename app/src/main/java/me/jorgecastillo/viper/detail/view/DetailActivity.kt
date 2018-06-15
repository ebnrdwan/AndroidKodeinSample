package me.jorgecastillo.viper.detail.view

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_detail.*
import me.jorgecastillo.viper.R
import me.jorgecastillo.viper.common.di.InjectedActivity
import me.jorgecastillo.viper.detail.di.detailActivityModule
import me.jorgecastillo.viper.detail.presenter.DetailPresenter
import me.jorgecastillo.viper.common.domain.model.Photo
import me.jorgecastillo.viper.common.view.getBitmapUri
import org.kodein.di.Kodein
import org.kodein.di.generic.instance
import java.text.SimpleDateFormat
import java.util.*

class DetailActivity : InjectedActivity(), DetailPresenter.View {

  private val presenter by instance<DetailPresenter>()
  private val photoId by lazy {
    intent?.extras?.getString(EXTRA_ID) ?: NO_ID
  }

  override fun activityModule() = Kodein.Module {
    import(detailActivityModule(photoId))
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_detail)
    setSupportActionBar(toolbar)
    setupShareButton()
  }

  override fun onResume() {
    super.onResume()
    presenter.resume(this)
  }

  override fun onPause() {
    super.onPause()
    presenter.pause()
  }

  private fun setupShareButton() {
    fab.setOnClickListener {
      presenter.onShareButtonClicked()
    }
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
    R.id.action_settings -> true
    else -> super.onOptionsItemSelected(item)
  }

  override fun onCreateOptionsMenu(menu: Menu): Boolean {
    menuInflater.inflate(R.menu.menu_detail, menu)
    return true
  }

  override fun showLoading() {
    loading.visibility = View.VISIBLE
  }

  override fun hideLoading() {
    loading.visibility = View.GONE
  }

  override fun showShareChooser() {
    val shareIntent = Intent()
    val share = picture.getBitmapUri()
    shareIntent.action = "android.intent.action.SEND"
    shareIntent.putExtra("android.intent.extra.STREAM", share)
    shareIntent.type = "image/jpeg"
    (getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager).primaryClip =
        ClipData.newPlainText(description.text, description.text)
    startActivity(Intent.createChooser(shareIntent, "Share"))
  }

  override fun renderPhoto(photo: Photo) {
    Picasso.get()
        .load(photo.url)
        .into(picture)

    content.visibility = View.VISIBLE
    titleView.text = photo.author
    unsplashLink.text = getString(R.string.unsplash)

    val date = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault()).parse(photo.created_at)
    publishedAt.text = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(date)
  }

  override fun displayLoadingPhotoError() {
    Snackbar.make(toolbar, R.string.loading_photo_error, Snackbar.LENGTH_SHORT).show()
  }

  companion object {
    const val EXTRA_ID = "EXTRA_ID"
    const val NO_ID = "NO_ID"

    fun getIntent(source: Context, id: String): Intent {
      val intent = Intent(source, DetailActivity::class.java)
      intent.putExtra(EXTRA_ID, id)
      return intent
    }
  }
}