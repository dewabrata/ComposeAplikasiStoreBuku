package com.adl.aplikasistorebuku

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel


import androidx.lifecycle.ViewModelProvider
import coil.compose.AsyncImage
import com.adl.aplikasistorebuku.data.Book
import com.adl.aplikasistorebuku.repo.BooksRepo
import com.adl.aplikasistorebuku.repo.OnFailure
import com.adl.aplikasistorebuku.repo.OnSuccess
import com.adl.aplikasistorebuku.ui.theme.AplikasiStoreBukuTheme
import com.adl.aplikasistorebuku.viewmodel.BookViewModel
import kotlinx.coroutines.flow.asStateFlow
import okhttp3.internal.threadFactory
import java.lang.IllegalStateException

class MainActivity : ComponentActivity() {

    val bookViewModel by viewModels<BookViewModel>(factoryProducer = { BookViewModelFactory(
        BooksRepo()
    ) })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AplikasiStoreBukuTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    BookOutlet(bookViewModel = bookViewModel)
                }
            }
        }
    }
}


@Composable
fun BookOutlet(
    bookViewModel: BookViewModel
){

    when(val bookList = bookViewModel.bookStateFlow.asStateFlow().collectAsState().value){
        is OnFailure ->{

        }
        is OnSuccess ->{
            val listOfBook = bookList.querySnapshot?.toObjects(Book::class.java)
            listOfBook?.let{
                Column {
                   LazyColumn(modifier = Modifier.fillMaxWidth().padding(16.dp)){
                       items(listOfBook){
                           Card(modifier = Modifier
                               .fillMaxWidth()
                               .padding(16.dp),
                               shape = RoundedCornerShape(16.dp), elevation = 5.dp){
                               BookItem(it)
                           }
                       }
                   }



                }
            }
        }
    }

}

@Composable
fun BookItem(book:Book){
    var showBookSipnosis by remember { mutableStateOf(false)}


    Column(modifier = Modifier.clickable {
        showBookSipnosis = showBookSipnosis.not()

    }) {

        Row(modifier = Modifier.padding(12.dp)){

            AsyncImage(
                model = book.gambar,

                contentDescription = null,
                modifier = Modifier.size(50.dp),
                contentScale = ContentScale.Fit
            )
            
            Column(modifier = Modifier.padding(5.dp,1.dp,0.dp,0.dp)) {
                Text(text = book.judul, style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 18.sp))
                Text(text = book.harga, style = TextStyle(fontWeight = FontWeight.Light, fontSize = 12.sp))
            }
        }

        AnimatedVisibility(visible = showBookSipnosis) {
            Text(text = book.sipnosis,
                style = TextStyle(fontWeight = FontWeight.SemiBold,
                    fontStyle = FontStyle.Italic, fontSize = 11.sp),
            modifier = Modifier.padding(start = 24.dp, end = 24.dp, bottom = 16.dp))
        }

    }
}

class BookViewModelFactory (val booksRepo: BooksRepo):ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(BookViewModel::class.java)){
            return  BookViewModel(booksRepo) as T
        }
        throw IllegalStateException()
    }

}