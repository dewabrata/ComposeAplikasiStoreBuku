package com.adl.aplikasistorebuku.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adl.aplikasistorebuku.repo.BookResponse
import com.adl.aplikasistorebuku.repo.BooksRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class BookViewModel(val booksRepo: BooksRepo):ViewModel() {

    val bookStateFlow = MutableStateFlow<BookResponse?>(null)

    init{
        viewModelScope.launch {
            booksRepo.getBookDetails().collect(){
                bookStateFlow.value = it
            }
        }
    }

    fun getBookInfo() = booksRepo.getBookDetails()
}