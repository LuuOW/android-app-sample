package com.lucaskempe.cropy.services

import android.content.Context
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.gson.annotations.SerializedName
import com.lucaskempe.cropy.services.models.ExchangeRateResponse
import com.lucaskempe.cropy.services.repository.LocalDataSource
import com.lucaskempe.cropy.services.repository.LocalDataSourceImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.isA
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`

@RunWith(AndroidJUnit4::class)
class LocalDataSourceTest {

    @get:Rule
    val coroutineTestRule = TestCoroutineDispatcher()

    private lateinit var mockContextLocalDataSource: Context
    private lateinit var localDataSourceUnderTest: LocalDataSource

    @Before
    fun setUp() {
        mockContextLocalDataSource = mock(Context::class.java)
        localDataSourceUnderTest = LocalDataSourceImpl(mockContextLocalDataSource)
    }

    @Test
    fun getLocalData_returnsDataFromLocalDataSource() = coroutineTestRule.runBlockingTest {
        // Given
        localDataSourceUnderTest.getLocalData()
        val job = Job().also {
            ExchangeRateResponse(
                provider = "me",
                warning = "",
                "",
                "",
                "",
                timeLastUpdated = 1,
                rates = mapOf()
            )
        }.isCompleted


        // When
        val result = localDataSourceUnderTest.getLocalData()

        // Then
    }
}
