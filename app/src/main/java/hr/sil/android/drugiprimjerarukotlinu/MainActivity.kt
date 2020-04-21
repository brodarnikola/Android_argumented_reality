package hr.sil.android.drugiprimjerarukotlinu

import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.ar.core.HitResult
import com.google.ar.core.Plane
import com.google.ar.core.TrackingState
import com.google.ar.sceneform.ux.ArFragment
import kotlinx.android.synthetic.main.activity_main.*
import com.google.ar.core.Trackable
import android.R.attr.fragment
import java.lang.ref.WeakReference
import android.R.attr.fragment
import androidx.appcompat.app.AlertDialog
import com.google.ar.sceneform.ux.TransformableNode
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.core.Anchor
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.Camera
import com.google.ar.sceneform.Node
import com.google.ar.sceneform.Sun


class MainActivity : AppCompatActivity() {

    private var fragment: ArFragment? = null
    private val pointer = PointerDrawable()
    private var isTracking: Boolean = false
    private var isHitting: Boolean = false

    private var modelLoader: ModelLoader? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fragment = getSupportFragmentManager().findFragmentById(R.id.sceneform_fragment) as ArFragment
        modelLoader = ModelLoader(WeakReference(this@MainActivity))

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        fragment!!.getArSceneView().scene.addOnUpdateListener { frameTime ->
            fragment!!.onUpdate(frameTime)
            onUpdate()
        }

        initializeGallery()
    }

    private fun initializeGallery() {
        val gallery = findViewById<LinearLayout>(R.id.gallery_layout)

        val andy = ImageView(this)
        andy.setImageResource(R.drawable.droid_thumb)
        andy.setContentDescription("andy")
        andy.setOnClickListener({ view -> addObject(Uri.parse("andy_dance.sfb")) })
        gallery.addView(andy)

        val cabin = ImageView(this)
        cabin.setImageResource(R.drawable.cabin_thumb)
        cabin.setContentDescription("cabin")
        cabin.setOnClickListener({ view -> addObject(Uri.parse("Cabin.sfb")) })
        gallery.addView(cabin)

        val house = ImageView(this)
        house.setImageResource(R.drawable.house_thumb)
        house.setContentDescription("house")
        house.setOnClickListener({ view -> addObject(Uri.parse("House.sfb")) })
        gallery.addView(house)

        val igloo = ImageView(this)
        igloo.setImageResource(R.drawable.igloo_thumb)
        igloo.setContentDescription("igloo")
        igloo.setOnClickListener({ view -> addObject(Uri.parse("igloo.sfb")) })
        gallery.addView(igloo)
    }

    private fun addObject(model: Uri) {

        val frame = fragment?.getArSceneView()?.arFrame
        val pt = getScreenCenter()
        val hits: List<HitResult>
        if (frame != null) {
            hits = frame.hitTest(pt.x.toFloat(), pt.y.toFloat())
            for (hit in hits) {
                val trackable = hit.trackable
                if (trackable is Plane && trackable.isPoseInPolygon(hit.hitPose)) {
                    modelLoader?.loadModel(hit.createAnchor(), model)
                    break

                }
            }
        }
    }

    fun addNodeToScene(anchor: Anchor, renderable: ModelRenderable) {

        val children = fragment?.getArSceneView()?.scene?.children
        if (children != null && children.size > 0) {
            for (deleteNode in children) {
                if (deleteNode is AnchorNode) {
                    if (deleteNode.anchor != null) {

                        println("Will it enter here 1")
                        //((AnchorNode) deleteNode).getAnchor().detach();
                        fragment?.getArSceneView()?.scene?.removeChild(deleteNode)
                    }
                }
                if (deleteNode !is Camera && deleteNode !is Sun) {

                    println("Will it enter here 2")
                    deleteNode.setParent(null)
                }
            }
        }

        val anchorNode = AnchorNode(anchor)
        val node = TransformableNode(fragment?.getTransformationSystem())
        node.renderable = renderable
        node.setParent(anchorNode)
        fragment?.getArSceneView()?.scene?.addChild(anchorNode)
        node.select()
    }

    fun onException(throwable: Throwable) {
        val builder = AlertDialog.Builder(this)
        builder.setMessage(throwable.message)
            .setTitle("Codelab error!")
        val dialog = builder.create()
        dialog.show()
        return
    }

    private fun onUpdate() {
        val trackingChanged = updateTracking()
        val contentView = findViewById<View>(android.R.id.content)
        if (trackingChanged) {
            if (isTracking) {
                contentView.getOverlay().add(pointer)
            } else {
                contentView.getOverlay().remove(pointer)
            }
            contentView.invalidate()
        }

        if (isTracking) {
            val hitTestChanged = updateHitTest()
            if (hitTestChanged) {
                pointer.setEnabled(isHitting)
                contentView.invalidate()
            }
        }
    }

    private fun updateTracking(): Boolean {
        val frame = fragment?.getArSceneView()?.arFrame
        val wasTracking = isTracking
        isTracking = frame != null && frame.camera.trackingState === TrackingState.TRACKING
        return isTracking !== wasTracking
    }

    private fun updateHitTest(): Boolean {
        val frame = fragment?.getArSceneView()?.arFrame
        val pt = getScreenCenter()
        val hits: List<HitResult>
        val wasHitting = isHitting
        isHitting = false
        if (frame != null) {
            hits = frame.hitTest(pt.x.toFloat(), pt.y.toFloat())
            for (hit in hits) {
                val trackable = hit.trackable
                if (trackable is Plane && (trackable as Plane).isPoseInPolygon(hit.hitPose)) {
                    isHitting = true
                    break
                }
            }
        }
        return wasHitting != isHitting
    }

    private fun getScreenCenter(): android.graphics.Point {
        val vw = findViewById<View>(android.R.id.content)
        return android.graphics.Point(vw.getWidth() / 2, vw.getHeight() / 2)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}
