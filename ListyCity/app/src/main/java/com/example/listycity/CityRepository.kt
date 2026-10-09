
package com.example.listycity

import androidx.compose.runtime.mutableStateListOf
import com.google.firebase.firestore.FirebaseFirestore

class CityRepository {

    private val db = FirebaseFirestore.getInstance()
    private val citiesRef = db.collection("cities")

    private val _cities = mutableStateListOf<City>()

    val cities: List<City>
        get() = _cities

    init {
        citiesRef.addSnapshotListener { snapshot, error ->
            if (error != null) {
                return@addSnapshotListener
            }

            if (snapshot != null) {
                _cities.clear()

                for (document in snapshot.documents) {
                    val name = document.getString("name") ?: ""
                    val province = document.getString("province") ?: ""

                    _cities.add(City(name, province))
                }
            }
        }
    }

    fun addCity(city: City) {
        citiesRef.document(city.name).set(
            hashMapOf(
                "name" to city.name,
                "province" to city.province
            )
        )
    }

    fun updateCity(oldCity: City, updatedCity: City) {
        citiesRef.document(oldCity.name).delete()

        addCity(updatedCity)
    }

    fun deleteCity(city: City) {
        citiesRef.document(city.name).delete()
    }
}
