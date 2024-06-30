package com.training.ecommerce.data.repository.special_sections

import com.training.ecommerce.data.models.SpecialSectionModel
import kotlinx.coroutines.flow.Flow

interface SpecialSectionsRepository {
    fun recommendProductsSection(): Flow<SpecialSectionModel?>
}