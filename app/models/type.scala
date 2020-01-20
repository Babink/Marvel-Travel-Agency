package models.Types

import models.Activity.Activities

case class Types(
    title: String,
    activity: Activities,
    images: Array[String],
    description: String
)
