Overview
--------

ebook-compiler is a simplified version of the teaching software compiler.
It only supports one template with text and images (optional).
All content info is saved to the encrypted sqlite database.

You can run

    ebook-compiler.exe -no-crypt

to disable database encryption.


Content database tables
-----------------------

* metainfo - Contains the following info:
  - version: content version
  - startup_fg_color: startup screen foreground (RGBA value)
  - startup_fg_color: startup screen background (RGBA value)
  - font: default font (you may use only first two values: name, size)
  - startup_image: startup screen image id (see media table description)

* area - Contains areas.
  - rowid - area ID.
  - fgcolor - area's text color (RGBA value).
  - bgcolor - area's background color (RGBA value).
  - title - area title.

* chapter - Contains chapters.
  - rowid - chapter ID.
  - area_id - parent area ID.
  - title - chapter title.
  - flags - number of chapter markers.

* topic - Contains topics.
  - rowid - topic ID.
  - chapter_id - parent chapter ID.
  - title - topic title.

* module - Contains topics modules.
  - rowid - module ID.
  - topic_id - parent topic ID.
  - title - module title.
  - thumbId - module image id, optional (see media table description).
  - templates - list of templates' IDs (see template table description).

* template - Contains modules templates.
  - rowid - template ID.
  - module_id - parent module ID.
  - text - template's HTML text.
  - images - list of images IDs for the template, optional
             (see media table description).

* media - Contains images for the templates.
  - rowid - image ID.
  - value - image itself.
