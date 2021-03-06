package kr.co.ddophi.autochangingwallpaper.MainActivity

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_recycler.view.*
import kr.co.ddophi.autochangingwallpaper.Album
import kr.co.ddophi.autochangingwallpaper.R

//여러 앨범들 보여주는 리사이클러뷰 구현
class CustomAdapter(recyclerViewInterface: MyRecyclerViewInterface, val activity: AppCompatActivity) : RecyclerView.Adapter<CustomAdapter.Holder>() {

    var albumData = mutableListOf<Album>()
    var currentServicePosition = -1
    private var recyclerViewInterface : MyRecyclerViewInterface? = null

    init{
        this.recyclerViewInterface = recyclerViewInterface
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_recycler, parent, false)
        return Holder(view, this.recyclerViewInterface!!)
    }

    override fun getItemCount(): Int {
        return albumData.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val album = albumData[position]
        holder.setAlbum(album)
    }

    inner class Holder(itemView: View, recyclerViewInterface: MyRecyclerViewInterface) : RecyclerView.ViewHolder(itemView) {

        var title : String = ""

        init{
            itemView.btnDelete.setOnClickListener { recyclerViewInterface.deleteButtonClicked(adapterPosition) }
            itemView.btnEdit.setOnClickListener {recyclerViewInterface.editButtonClicked(adapterPosition)}
            itemView.btnSelect.setOnClickListener {recyclerViewInterface.selectButtonClicked(adapterPosition)}
            itemView.albumImage.setOnClickListener {recyclerViewInterface.representImageClicked(adapterPosition)}

            // 키보드 완료 버튼을 눌렀을 때 동작 (저장, 키보드 내리기, 포커스 해제)
            itemView.albumTitle.setOnEditorActionListener { v, actionId, event ->
                recyclerViewInterface.albumTitleClicked(adapterPosition, title)
                val im : InputMethodManager = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                im.hideSoftInputFromWindow(itemView.albumTitle.windowToken, 0)
                itemView.albumTitle.clearFocus()
                false
            }
            //앨범 제목 변경할 때 어떤 글자를 썼는지 수시로 확인
            itemView.albumTitle.addTextChangedListener(object: TextWatcher{
                override fun afterTextChanged(s: Editable?) {
                    title = s.toString()
                }
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            })
        }

        fun setAlbum(album : Album) {
            itemView.albumTitle.setText(album.albumTitle)
            itemView.pictureCount.text = "${album.pictureCount} photos"
            Glide.with(activity).load(album.representImage).placeholder(R.drawable.progress_animation).into(itemView.albumImage)
            if(currentServicePosition == adapterPosition){
                itemView.albumchecked.visibility = View.VISIBLE
            }else{
                itemView.albumchecked.visibility = View.INVISIBLE
            }
        }
    }
}

