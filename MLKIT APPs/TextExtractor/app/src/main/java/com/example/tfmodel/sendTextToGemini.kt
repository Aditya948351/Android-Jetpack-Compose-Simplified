import androidx.compose.runtime.Composable
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import okio.IOException

@Composable
fun sendTextToGemini(ocrText: String, onResult: (String) -> Unit) {
    val GEMINI_API_KEY = "AIzaSyCmrM28rx2cwmdigJ0Bqb0fqUXTt5Vx7NE"
    val apiUrl = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=GEMINI_API_KEY"
    val prompt = """
        You are a professional text formatter.
        Given this raw OCR extracted text, fix spelling, grammar, and return it in well-formatted paragraphs:
        
        "$ocrText"
    """.trimIndent()

    val json = """{"contents":[{"parts":[{"text":"$prompt"}]}]}"""
    val client = OkHttpClient()
    val body = json.toRequestBody("application/json".toMediaTypeOrNull())

    val request = Request.Builder()
        .url(apiUrl)
        .post(body)
        .build()

    client.newCall(request).enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) = onResult("Gemini Error: ${e.message}")
        override fun onResponse(call: Call, response: Response) {
            response.body?.string()?.let {
                val formatted = Regex("\"text\"\\s*:\\s*\"(.*?)\"")
                    .find(it)?.groupValues?.get(1)?.replace("\\n", "\n") ?: "No result"
                onResult(formatted)
            } ?: onResult("Empty Gemini Response")
        }
    })
}
