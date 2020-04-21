package hr.sil.android.drugiprimjerarukotlinu

import android.net.Uri
import android.util.Log
import com.google.ar.core.Anchor
import com.google.ar.sceneform.rendering.ModelRenderable
import java.lang.ref.WeakReference


class ModelLoader constructor( weakReference: WeakReference<MainActivity>) {

    private var owner: WeakReference<MainActivity>? = weakReference
    private val TAG = "ModelLoader"

    fun loadModel(anchor: Anchor, uri: Uri) {
        if (owner?.get() == null) {
            Log.d(TAG, "Activity is null.  Cannot load model.")
            return
        }
        ModelRenderable.builder()
            .setSource(owner?.get(), uri)
            .build()
            .handle(ModelRenderable@{ renderable, throwable ->
                val activity = owner!!.get()
                if (activity == null) {
                    null
                } else if (throwable != null) {
                    activity.onException(throwable)
                } else {
                    activity.addNodeToScene(anchor, renderable)
                }
                null
            })

        return
    }

}