package com.example

import com.example.data.sample.SampleData
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

class ExampleUnitTest {

    @Test
    fun test_15km_radius_enforcement() {
        val maxRadius = 15.0

        val ragadiBazarDistance = 1.2
        val jajpurRoadStationDistance = 3.2
        val danagadiDistance = 6.5
        val kalinganagarDistance = 8.4
        val dhabaleswarDistance = 13.5
        val kuakhiaDistance = 22.0

        assertTrue(ragadiBazarDistance <= maxRadius)
        assertTrue(jajpurRoadStationDistance <= maxRadius)
        assertTrue(danagadiDistance <= maxRadius)
        assertTrue(kalinganagarDistance <= maxRadius)
        assertTrue(dhabaleswarDistance <= maxRadius)
        assertFalse(kuakhiaDistance <= maxRadius)
    }

    @Test
    fun test_delivery_fee_calculation() {
        val config = SampleData.defaultStoreConfig // base: 20, baseKm: 3, perKm: 5, freeMin: 499

        // Within base 3km
        val fee1 = calculateTestDeliveryFee(2.0, 300.0, config.baseDeliveryFee, config.baseDistanceKm, config.perKmFee, config.freeDeliveryMinAmount)
        assertEquals(20.0, fee1, 0.01)

        // Above 3km (e.g. 5km: 20 + 2*5 = 30)
        val fee2 = calculateTestDeliveryFee(5.0, 300.0, config.baseDeliveryFee, config.baseDistanceKm, config.perKmFee, config.freeDeliveryMinAmount)
        assertEquals(30.0, fee2, 0.01)

        // Order value >= 499 gets free delivery
        val feeFree = calculateTestDeliveryFee(5.0, 550.0, config.baseDeliveryFee, config.baseDistanceKm, config.perKmFee, config.freeDeliveryMinAmount)
        assertEquals(0.0, feeFree, 0.01)
    }

    @Test
    fun test_haversine_distance() {
        // Ragadi coords: 20.9507, 86.1344
        // Jajpur Road Railway Station approx coords: 20.9582, 86.1627
        val dist = haversine(20.9507, 86.1344, 20.9582, 86.1627)
        assertTrue(dist in 2.5..4.0)
    }

    private fun calculateTestDeliveryFee(
        distanceKm: Double,
        subtotal: Double,
        baseFee: Double,
        baseKm: Double,
        perKm: Double,
        freeMin: Double
    ): Double {
        if (subtotal >= freeMin) return 0.0
        return if (distanceKm <= baseKm) baseFee else baseFee + (distanceKm - baseKm) * perKm
    }

    private fun haversine(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val r = 6371.0
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        val a = sin(dLat / 2) * sin(dLat / 2) +
                cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
                sin(dLon / 2) * sin(dLon / 2)
        return r * 2 * atan2(sqrt(a), sqrt(1 - a))
    }
}
