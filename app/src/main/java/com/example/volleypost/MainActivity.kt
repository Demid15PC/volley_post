package com.example.volleypost

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.volleypost.ui.theme.*
import org.json.JSONException
import org.json.JSONObject
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*


class MainActivity : ComponentActivity() {
       override fun onCreate(savedInstanceState: Bundle?) {
              super.onCreate(savedInstanceState)
              setContent {
                     VolleyPostTheme() {
                            Surface(
                                   modifier = Modifier.fillMaxSize(),
                                   color = MaterialTheme.colors.background
                            ) {
                                   PostDataUsingVolley()
                            }
                     }
              }
       }
}

@Composable
fun PostDataUsingVolley() {
       val context = LocalContext.current

       val userName = remember {
              mutableStateOf(TextFieldValue())
       }
       val userJob = remember {
              mutableStateOf(TextFieldValue())
       }
       val response = remember {
              mutableStateOf("")
       }

       Column(
              modifier = Modifier
                     .fillMaxSize()
                     .fillMaxHeight()
                     .fillMaxWidth(),

              verticalArrangement = Arrangement.Center,
              horizontalAlignment = Alignment.CenterHorizontally
       ) {
              Text(
                     text = "Volley POST",
                     color = GreenColor,
                     fontSize = 20.sp,
                     fontFamily = FontFamily.Default,
                     fontWeight = FontWeight.Bold,
                     textAlign = TextAlign.Center
              )
              Spacer(modifier = Modifier.height(5.dp))
              TextField(
                     value = userName.value,
                     onValueChange = {
                            userName.value = it
                     },
                     placeholder = { Text(text = "Имя") },
                     modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                     textStyle = TextStyle(color = Color.Black, fontSize = 15.sp),
                     singleLine = true,
              )
              Spacer(modifier = Modifier.height(5.dp))
              TextField(
                     value = userJob.value,
                     onValueChange = {
                            userJob.value = it
                     },
                     placeholder = { Text(text = "Работа") },
                     modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                     textStyle = TextStyle(color = Color.Black, fontSize = 15.sp),
                     singleLine = true,
              )
              Spacer(modifier = Modifier.height(10.dp))
              Button(
                     onClick = {
                            postData(userName.value.text, userJob.value.text, context, response)
                     },
                     modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
              ) {
                     Text(text = "Запостить", modifier = Modifier.padding(8.dp))
              }
              Spacer(modifier = Modifier.height(20.dp))
              Text(
                     text = response.value,
                     color = Color.Black,
                     fontSize = 20.sp,
                     fontWeight = FontWeight.Bold, modifier = Modifier
                            .padding(10.dp)
                            .fillMaxWidth(),
                     textAlign = TextAlign.Center
              )
       }
}

private fun postData(
       userName: String,
       job: String,
       context: Context,
       res: MutableState<String>
) {
       var url = "https://reqres.in/api/users"
       val queue = Volley.newRequestQueue(context)

       val request: StringRequest =
              object : StringRequest(
                     Request.Method.POST,
                     url,
                     { response ->



                            try {
                                   val mainObject = JSONObject(response)

                                   // creating a string for our output.
                                   val result = "User Name : " + mainObject.getString("name") + "\n" + "Job : " + mainObject.getString(
                                                 "job")
                                   val date = mainObject.getString("createdAt")
                                   val zoned = ZonedDateTime.parse(date.toString())
                                   val formatted = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm").format(zoned)


                                   Toast.makeText(context, "$result $formatted", Toast.LENGTH_SHORT).show()

                                   res.value = result
                            } catch (e: JSONException) {
                                   e.printStackTrace()// вывод ишибки в лог
                            }
                     },
                     {

                            Toast.makeText(context, "Fail to post data..", Toast.LENGTH_SHORT).show()
                     }) {
                     override fun getParams(): Map<String, String>? {

                            val params: MutableMap<String, String> = HashMap()

                            params["name"] = userName
                            params["job"] = job

                            return params
                     }
              }
       queue.add(request)
}