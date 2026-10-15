package id.arisan.ku

import android.app.Activity
import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.*
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : Activity() {
    private val green = Color.rgb(7, 134, 91)
    private val darkGreen = Color.rgb(5, 94, 64)
    private val paleGreen = Color.rgb(230, 247, 239)
    private val ink = Color.rgb(27, 40, 39)
    private val prefs by lazy { getSharedPreferences("arisanku_data", MODE_PRIVATE) }
    private val members = mutableListOf<String>()
    private val winners = mutableListOf<JSONObject>()
    private lateinit var root: LinearLayout
    private lateinit var resultText: TextView
    private lateinit var countText: TextView
    private lateinit var historyBox: LinearLayout
    private lateinit var nameInput: EditText
    private var groupName = "Arisan Keluarga"
    private var amount = "500000"
    private var preventRepeat = true


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = darkGreen
        window.navigationBarColor = Color.WHITE
        loadData()
        render()
    }

    private fun loadData() {
        groupName = prefs.getString("group", "Arisan Keluarga") ?: "Arisan Keluarga"
        amount = prefs.getString("amount", "500000") ?: "500000"
        preventRepeat = prefs.getBoolean("prevent_repeat", true)
        val a = JSONArray(prefs.getString("members", "[]"))
        for (i in 0 until a.length()) members.add(a.optString(i))
        val h = JSONArray(prefs.getString("history", "[]"))
        for (i in 0 until h.length()) winners.add(h.optJSONObject(i) ?: JSONObject())
        if (members.isEmpty()) members.addAll(listOf("Budi", "Siti", "Andi", "Rina"))
    }

    private fun saveData() {
        val a = JSONArray(); members.forEach { a.put(it) }
        val h = JSONArray(); winners.forEach { h.put(it) }
        prefs.edit().putString("group", groupName).putString("amount", amount)
            .putBoolean("prevent_repeat", preventRepeat)
            .putString("members", a.toString()).putString("history", h.toString()).apply()
    }

    private fun render() {
        root = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL; setBackgroundColor(Color.rgb(247,250,248)) }
        val scroll = ScrollView(this)
        val page = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL; setPadding(dp(20), dp(16), dp(20), dp(28)) }
        scroll.addView(page)
        root.addView(scroll, ViewGroup.LayoutParams(-1, 0).apply { height = 0; width = -1 })
        (root.layoutParams as? LinearLayout.LayoutParams)?.weight = 1f
        setContentView(root)
        root.removeAllViews()
        root.addView(scroll, LinearLayout.LayoutParams(-1, 0, 1f))

        val brand = TextView(this).apply { text = "✿  ArisanKu"; textSize = 27f; setTextColor(green); setTypeface(null, 1); setPadding(0, dp(4), 0, dp(4)) }
        page.addView(brand)
        page.addView(label("Mudah • Adil • Transparan", 14, Color.GRAY))
        page.addView(space(18))

        val titleCard = card(paleGreen)
        titleCard.addView(label(groupName, 21, darkGreen, true))
        titleCard.addView(label("Undian arisan sederhana, langsung dari HP Anda.", 14, darkGreen))
        page.addView(titleCard)
        page.addView(space(18))
        page.addView(sectionTitle("Daftar peserta"))
        nameInput = EditText(this).apply {
            hint = "Ketik nama peserta"; textSize = 16f; singleLine = true
            setPadding(dp(12), dp(8), dp(12), dp(8)); setBackgroundColor(Color.WHITE)
        }
        page.addView(nameInput, LinearLayout.LayoutParams(-1, dp(52)))
        val addBtn = button("+ Tambah peserta") { addMember() }
        page.addView(addBtn, matchWrap())
        countText = label("", 14, Color.GRAY)
        page.addView(countText)
        val memberList = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL }
        members.forEachIndexed { index, name ->
            val row = LinearLayout(this).apply { orientation = LinearLayout.HORIZONTAL; gravity = Gravity.CENTER_VERTICAL; setPadding(dp(12), dp(8), dp(6), dp(8)); setBackgroundColor(Color.WHITE) }
            row.addView(label("${index + 1}.  $name", 16, ink), LinearLayout.LayoutParams(0, -2, 1f))
            val remove = TextView(this).apply { text = "Hapus"; textSize = 13f; setTextColor(Color.RED); setPadding(dp(8), dp(8), dp(8), dp(8)); setOnClickListener { members.removeAt(index); saveData(); render() } }
            row.addView(remove)
            memberList.addView(row, matchWrap())
            memberList.addView(space(1))
        }
        page.addView(memberList)
        page.addView(space(18))

        page.addView(sectionTitle("Nominal hadiah (Rp)"))
        val amountInput = EditText(this).apply { setText(amount); inputType = 2; textSize = 16f; hint = "500000"; setPadding(dp(12), dp(8), dp(12), dp(8)); setBackgroundColor(Color.WHITE) }
        page.addView(amountInput, LinearLayout.LayoutParams(-1, dp(50)))
        val groupInput = EditText(this).apply { setText(groupName); textSize = 16f; hint = "Nama arisan"; setPadding(dp(12), dp(8), dp(12), dp(8)); setBackgroundColor(Color.WHITE) }
        page.addView(space(10)); page.addView(sectionTitle("Nama kelompok")); page.addView(groupInput, LinearLayout.LayoutParams(-1, dp(50)))
        val repeatCheck = CheckBox(this).apply { text = "Jangan pilih pemenang yang sudah menang"; isChecked = preventRepeat; setTextColor(ink); buttonTintList = android.content.res.ColorStateList.valueOf(green) }
        page.addView(repeatCheck)
        page.addView(space(12))
        resultText = TextView(this).apply { text = "Siapa pemenangnya?"; textSize = 23f; setTypeface(null, 1); setTextColor(darkGreen); gravity = Gravity.CENTER; setPadding(dp(12), dp(26), dp(12), dp(26)); setBackgroundColor(paleGreen) }
        page.addView(resultText, matchWrap())
        page.addView(space(10))
        page.addView(button("🎲  UNDI PEMENANG", green) {
            groupName = groupInput.text.toString().trim().ifBlank { "Arisan Keluarga" }
            amount = amountInput.text.toString().trim().ifBlank { "0" }
            preventRepeat = repeatCheck.isChecked
            drawWinner()
        }, matchWrap())
        page.addView(space(20))
        page.addView(sectionTitle("Riwayat pemenang"))
        historyBox = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL }
        page.addView(historyBox, matchWrap())
        refreshHistory()
        page.addView(space(10))
        page.addView(button("Hapus riwayat undian", Color.rgb(180,55,55)) {
            AlertDialog.Builder(this).setTitle("Hapus riwayat?").setMessage("Riwayat pemenang akan dihapus permanen.")
                .setNegativeButton("Batal", null).setPositiveButton("Hapus") { _, _ -> winners.clear(); saveData(); refreshHistory() }.show()
        }, matchWrap())
        countText.text = "${members.size} peserta terdaftar"
    }

    private fun addMember() {
        val name = nameInput.text.toString().trim()
        if (name.isBlank()) { toast("Masukkan nama peserta terlebih dahulu."); return }
        if (members.any { it.equals(name, true) }) { toast("Nama peserta sudah ada."); return }
        members.add(name); saveData(); render()
    }

    private fun drawWinner() {
        if (members.size < 2) { toast("Tambahkan minimal 2 peserta."); return }
        val previous = winners.map { it.optString("name") }.toSet()
        val eligible = if (preventRepeat) members.filter { it !in previous } else members.toList()
        if (eligible.isEmpty()) { toast("Semua peserta sudah menang. Hapus riwayat atau nonaktifkan pembatasan."); return }
        val winner = eligible.random()
        val date = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale("id", "ID")).format(Date())
        val entry = JSONObject().put("name", winner).put("group", groupName).put("amount", amount).put("date", date)
        winners.add(0, entry)
        saveData()
        resultText.text = "🏆\n$winner\nRp ${formatRupiah(amount)}"
        refreshHistory()
        toast("Selamat kepada $winner!")
    }

    private fun refreshHistory() {
        if (!::historyBox.isInitialized) return
        historyBox.removeAllViews()
        if (winners.isEmpty()) {
            historyBox.addView(label("Belum ada hasil undian. Hasil akan tersimpan di perangkat ini.", 14, Color.GRAY))
            return
        }
        winners.forEachIndexed { index, item ->
            val row = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL; setPadding(dp(12), dp(12), dp(12), dp(12)); setBackgroundColor(Color.WHITE) }
            row.addView(label("🏆  ${item.optString("name", "-")}", 17, darkGreen, true))
            row.addView(label("${item.optString("group", groupName)} • Rp ${formatRupiah(item.optString("amount", "0"))}", 14, ink))
            row.addView(label(item.optString("date", ""), 12, Color.GRAY))
            historyBox.addView(row, matchWrap()); historyBox.addView(space(6))
        }
    }

    private fun formatRupiah(raw: String): String = (raw.toLongOrNull() ?: 0L).toString().reversed().chunked(3).joinToString(".").reversed()
    private fun sectionTitle(text: String) = label(text, 18, ink, true).apply { setPadding(0, dp(4), 0, dp(8)) }
    private fun label(text: String, size: Int, color: Int, bold: Boolean = false) = TextView(this).apply { this.text = text; textSize = size.toFloat(); setTextColor(color); if (bold) setTypeface(null, 1) }
    private fun card(color: Int) = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL; setPadding(dp(16), dp(16), dp(16), dp(16)); setBackgroundColor(color) }
    private fun button(text: String, color: Int = green, action: () -> Unit) = Button(this).apply { this.text = text; isAllCaps = false; textSize = 15f; setTextColor(Color.WHITE); backgroundTintList = android.content.res.ColorStateList.valueOf(color); setOnClickListener { action() } }
    private fun space(height: Int) = View(this).apply { layoutParams = LinearLayout.LayoutParams(1, dp(height)) }
    private fun matchWrap() = LinearLayout.LayoutParams(-1, -2).apply { bottomMargin = dp(4) }
    private fun dp(value: Int) = (value * resources.displayMetrics.density).toInt()
    private fun toast(message: String) = Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}
