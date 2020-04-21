package hr.sil.android.drugiprimjerarukotlinu

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.opengl.ETC1.getHeight
import android.opengl.ETC1.getWidth



class PointerDrawable : Drawable() {

    private val paint = Paint()
    private var enabled: Boolean = false

    fun isEnabled(): Boolean {
        return enabled
    }

    fun setEnabled(enabled: Boolean) {
        this.enabled = enabled
    }

    /**
     * Draw in its bounds (set via setBounds) respecting optional effects such
     * as alpha (set via setAlpha) and color filter (set via setColorFilter).
     *
     * @param canvas The canvas to draw into
     */
    override fun draw(canvas: Canvas) {
        val cx = canvas.width / 2
        val cy = canvas.height / 2
        if (enabled) {
            paint.color = Color.GREEN
            canvas.drawCircle(cx.toFloat(), cy.toFloat(), 10f, paint)
        } else {
            paint.color = Color.GRAY
            canvas.drawText("X", cx.toFloat(), cy.toFloat(), paint)
        }
    }

    /**
     * Specify an alpha value for the drawable. 0 means fully transparent, and
     * 255 means fully opaque.
     */
    override fun setAlpha(alpha: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    /**
     * Return the opacity/transparency of this Drawable.  The returned value is
     * one of the abstract format constants in
     * [android.graphics.PixelFormat]:
     * [android.graphics.PixelFormat.UNKNOWN],
     * [android.graphics.PixelFormat.TRANSLUCENT],
     * [android.graphics.PixelFormat.TRANSPARENT], or
     * [android.graphics.PixelFormat.OPAQUE].
     *
     *
     * An OPAQUE drawable is one that draws all all content within its bounds, completely
     * covering anything behind the drawable. A TRANSPARENT drawable is one that draws nothing
     * within its bounds, allowing everything behind it to show through. A TRANSLUCENT drawable
     * is a drawable in any other state, where the drawable will draw some, but not all,
     * of the content within its bounds and at least some content behind the drawable will
     * be visible. If the visibility of the drawable's contents cannot be determined, the
     * safest/best return value is TRANSLUCENT.
     *
     *
     * Generally a Drawable should be as conservative as possible with the
     * value it returns.  For example, if it contains multiple child drawables
     * and only shows one of them at a time, if only one of the children is
     * TRANSLUCENT and the others are OPAQUE then TRANSLUCENT should be
     * returned.  You can use the method [.resolveOpacity] to perform a
     * standard reduction of two opacities to the appropriate single output.
     *
     *
     * Note that the returned value does not necessarily take into account a
     * custom alpha or color filter that has been applied by the client through
     * the [.setAlpha] or [.setColorFilter] methods. Some subclasses,
     * such as [BitmapDrawable], [ColorDrawable], and [GradientDrawable],
     * do account for the value of [.setAlpha], but the general behavior is dependent
     * upon the implementation of the subclass.
     *
     * @return int The opacity class of the Drawable.
     *
     * @see android.graphics.PixelFormat
     */
    override fun getOpacity(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    /**
     * Specify an optional color filter for the drawable.
     *
     *
     * If a Drawable has a ColorFilter, each output pixel of the Drawable's
     * drawing contents will be modified by the color filter before it is
     * blended onto the render target of a Canvas.
     *
     *
     *
     * Pass `null` to remove any existing color filter.
     *
     *
     * **Note:** Setting a non-`null` color
     * filter disables [tint][.setTintList].
     *
     *
     * @param colorFilter The color filter to apply, or `null` to remove the
     * existing color filter
     */
    override fun setColorFilter(colorFilter: ColorFilter?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}