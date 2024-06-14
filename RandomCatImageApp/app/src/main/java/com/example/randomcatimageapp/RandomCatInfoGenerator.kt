package com.example.randomcatimageapp

// RandomCatInfoGenerator.kt
// This file contains the RandomCatInfoGenerator object
object RandomCatInfoGenerator {
    fun getRandomName(): String {
        val names = listOf("Whiskers", "Shadow", "Mittens", "Luna", "Simba", "Daisy", "Tiger", "Oreo", "Felix", "Milo", "Smokey", "Lucy", "Chloe", "Cleo", "Loki", "Ziggy", "Salem", "Misty", "Coco", "Jasper", "Boots", "Gizmo", "Angel", "Muffin", "Pumpkin", "Socks", "Pepper", "Mittens", "Molly", "Charlie", "Max", "Bella", "Oliver", "Lily", "Leo", "Sophie", "Jack", "Luna", "Milo", "Lucy", "Tiger", "Daisy", "Oscar", "Loki", "Zoe", "Sammy", "Misty", "Smokey", "Maggie", "Felix", "Cleo", "Salem", "Muffin", "Jasper", "Coco", "Angel", "Boots", "Gizmo", "Pepper", "Socks", "Misty", "Molly", "Charlie", "Max", "Bella", "Oliver", "Lily", "Leo", "Sophie", "Jack", "Luna", "Milo", "Lucy", "Tiger", "Daisy", "Oscar", "Loki", "Zoe", "Sammy", "Misty", "Smokey", "Maggie", "Felix", "Cleo", "Salem", "Muffin", "Jasper", "Coco", "Angel", "Boots", "Gizmo", "Pepper", "Socks", "Misty", "Molly", "Charlie", "Max", "Bella", "Oliver", "Lily", "Leo", "Sophie", "Jack", "Luna", "Milo", "Lucy", "Tiger", "Daisy", "Oscar", "Loki", "Zoe", "Sammy", "Misty", "Smokey", "Maggie", "Felix", "Cleo", "Salem", "Muffin", "Jasper", "Coco", "Angel", "Boots", "Gizmo", "Pepper", "Socks", "Misty", "Molly", "Charlie", "Max", "Bella", "Oliver", "Lily", "Leo", "Sophie")

        return names.random()
    }
}