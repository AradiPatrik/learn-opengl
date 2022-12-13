package com.aradipatrik.learn.opengl.models

object Meshes {
    object Box {
        val vertices = floatArrayOf(
            // top face
            -1f, 1f, -1f, 0.5f, 0.5f, // tl
            1f, 1f, -1f, 1f, 0.5f, // tr
            1f, 1f, 1f, 1f, 0f, // br
            1f, 1f, 1f, 1f, 0f, // br
            -1f, 1f, 1f, 0.5f, 0f, // bl
            -1f, 1f, -1f, 0.5f, 0.5f, // tl
            // bottom face
            -1f, -1f, -1f, 0f, 1f, // tl
            1f, -1f, -1f, 0.5f, 1f, // tr
            1f, -1f, 1f, 0.5f, 0.5f, // br
            1f, -1f, 1f, 0.5f, 0.5f, // br
            -1f, -1f, 1f, 0f, 0.5f, // bl
            -1f, -1f, -1f, 0f, 1f, // tl
            // right face
            1f, 1f, -1f, 0.5f, 0.5f, // ttr
            1f, 1f, 1f, 0f, 0.5f, // tbr
            1f, -1f, 1f, 0f, 0f, // bbr
            1f, -1f, 1f, 0f, 0f, // bbr
            1f, -1f, -1f, 0.5f, 0f, // btr
            1f, 1f, -1f, 0.5f, 0.5f, // ttr
            // left face
            -1f, 1f, -1f, 0.5f, 0.5f, // ttl
            -1f, 1f, 1f, 0f, 0.5f, // tbl
            -1f, -1f, 1f, 0f, 0f, // bbl
            -1f, -1f, 1f, 0f, 0f, // bbl
            -1f, -1f, -1f, 0.5f, 0f, // btl
            -1f, 1f, -1f, 0.5f, 0.5f, // ttl
            // front face
            1f, 1f, 1f, 0.5f, 0.5f, // tbr
            -1f, 1f, 1f, 0f, 0.5f, // tbl
            -1f, -1f, 1f, 0f, 0f, // bbl
            -1f, -1f, 1f, 0f, 0f, // bbl
            1f, -1f, 1f, 0.5f, 0f, // bbr
            1f, 1f, 1f, 0.5f, 0.5f, // tbr
            // back face
            -1f, 1f, -1f, 0.5f, 0.5f, // ttl
            1f, 1f, -1f, 0f, 0.5f, // ttr
            1f, -1f, -1f, 0f, 0f, // btr
            1f, -1f, -1f, 0f, 0f, // btr
            -1f, -1f, -1f, 0.5f, 0f, // btl
            -1f, 1f, -1f, 0.5f, 0.5f, // ttl
        )
    }
}