package at.fhooe.mc.fitcom.ui.nfc

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import at.fhooe.mc.fitcom.R
import at.fhooe.mc.fitcom.databinding.FragmentNfcBinding
import pl.droidsonroids.gif.GifDrawable

class NFCFragment : Fragment() {

    private var _binding: FragmentNfcBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var gif: GifDrawable
    private var flag: Boolean = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(NFCViewModel::class.java)


        _binding = FragmentNfcBinding.inflate(inflater, container, false)
        val root: View = binding.root


        gif = GifDrawable(resources, R.drawable.lock)
        binding.fragmentNfcLockGifImageView.setImageDrawable(gif)


        val textView: TextView = binding.textHome
        homeViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }


        gif.stop()


        binding.fragmentNfcGifStart.setOnClickListener {

            if(flag) {
                gif = GifDrawable(resources, R.drawable.lock)
                binding.fragmentNfcLockGifImageView.setImageDrawable(gif)
                gif.start()
                flag = false
            } else {
                gif = GifDrawable(resources, R.drawable.unlock)
                binding.fragmentNfcLockGifImageView.setImageDrawable(gif)
                gif.start()
                flag = true
            }
        }



        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}