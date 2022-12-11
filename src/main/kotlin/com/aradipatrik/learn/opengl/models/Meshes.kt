package com.aradipatrik.learn.opengl.models

object Meshes {
    object Box {
        val vertices = floatArrayOf(
            -1.0f, 1.0f, -1.0f, // ttl 0
            1.0f, 1.0f, -1.0f, // ttr 1
            1.0f, 1.0f, 1.0f, // tbr 2
            -1.0f, 1.0f, 1.0f, // tbl 3
            -1.0f, -1.0f, -1.0f, // btl 4
            1.0f, -1.0f, -1.0f, // btr 5
            1.0f, -1.0f, 1.0f, // bbr 6
            -1.0f, -1.0f, 1.0f, // bbl 7
        )

        val elements = intArrayOf(
            0, 1, 2, 2, 3, 0, // top face
            4, 5, 6, 6, 7, 4, // bottom face
            1, 2, 6, 6, 5, 1, // right face
            0, 3, 7, 7, 4, 0, // left face
            3, 2, 6, 6, 7, 3, // front face
            0, 1, 5, 5, 4, 0, // back face
        )
    }

    object Plane {
        val vertices = floatArrayOf(
            -1.0f, 1.0f, 0.0f, // tl 0
            1.0f, 1.0f, 0.0f, // tr 1
            1.0f, -1.0f, 0.0f, // br 2
            -1.0f, -1.0f, 0.0f, // bl 3
        )

        val elements = intArrayOf(
            0, 1, 2, 2, 3, 0
        )
    }
}