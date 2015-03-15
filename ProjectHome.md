This project is an attempt to render <a href='http://developers.cloudmade.com/wiki/vector-stream-server'>CloudMade SVG</a> maps.

It's not as successful as I'd hoped because it's rather slow. I had two original intentions:

  * to do a 3d render in combination with <a href='http://developers.cloudmade.com/projects/show/routing-http-api'>CloudMade routing</a> (hence the project name).
  * to do an osmdroid renderer that is fast and is better at zooming.

Unfortunately it doesn't really succeed at either of these, so I decided to open-source it and maybe it's useful for someone. It's also an example of how to use a custom renderer for osmdroid.

There are two activities:

  * SavageRouter : the main activity. Uses <a href='http://code.google.com/p/osmdroid/'>osmdroid</a> with a custom renderer for CloudMade SVG tiles.
  * TestActivity : just renderers a hard-coded SVG file. Also rotates to simulate perspective.

![http://savage-router.googlecode.com/files/SavageRouter.png](http://savage-router.googlecode.com/files/SavageRouter.png)
![http://savage-router.googlecode.com/files/TestActivity.png](http://savage-router.googlecode.com/files/TestActivity.png)
![http://savage-router.googlecode.com/files/TestActivityRotated.png](http://savage-router.googlecode.com/files/TestActivityRotated.png)
