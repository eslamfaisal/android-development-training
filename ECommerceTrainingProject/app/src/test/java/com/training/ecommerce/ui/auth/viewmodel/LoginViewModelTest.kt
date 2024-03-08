package com.training.ecommerce.ui.auth.viewmodel


import com.google.common.truth.Truth.assertThat
import com.training.ecommerce.MainDispatcherRule
import com.training.ecommerce.data.repository.user.UserDataStoreRepositoryFakeImpl
import com.training.ecommerce.ui.common.viewmodel.UserViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
@ExperimentalCoroutinesApi
class UserViewModelTest {

    @ExperimentalCoroutinesApi
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val userPreferencesRepository = UserDataStoreRepositoryFakeImpl()

    private lateinit var userViewModel: UserViewModel

    @Before
    fun setUp() {
        userViewModel = UserViewModel(userPreferencesRepository)
    }

    @Test
    fun testIsUserIsLoggedIn() = runTest {

        // Act
        val isLoggedIn = userViewModel.isUserLoggedIn().first()

        assertThat(isLoggedIn).isEqualTo(false)
    }

    @Test
    fun testSetUserIsLoggedIn() = runTest {
        userViewModel.setIsLoggedIn(true)

        val isLoggedIn = userViewModel.isUserLoggedIn().first()
        assertThat(isLoggedIn).isEqualTo(true)
    }

}

