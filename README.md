# savage-router
Automatically exported from code.google.com/p/savage-router

<p>This project is an attempt to render <a href="http://developers.cloudmade.com/wiki/vector-stream-server" rel="nofollow">CloudMade SVG</a> maps. </p><p>It&#x27;s not as successful as I&#x27;d hoped because it&#x27;s rather slow. I had two original intentions: </p><ul><li>to do a 3d render in combination with <a href="http://developers.cloudmade.com/projects/show/routing-http-api" rel="nofollow">CloudMade routing</a> (hence the project name). </li><li>to do an osmdroid renderer that is fast and is better at zooming. </li></ul><p>Unfortunately it doesn&#x27;t really succeed at either of these, so I decided to open-source it and maybe it&#x27;s useful for someone. It&#x27;s also an example of how to use a custom renderer for osmdroid. </p><p>There are two activities: </p><ul><li>SavageRouter : the main activity. Uses <a href="http://code.google.com/p/osmdroid/" rel="nofollow">osmdroid</a> with a custom renderer for CloudMade SVG tiles. </li><li>TestActivity : just renderers a hard-coded SVG file. Also rotates to simulate perspective. </li></ul><p><img src="http://savage-router.googlecode.com/files/SavageRouter.png" /> <img src="http://savage-router.googlecode.com/files/TestActivity.png" /> <img src="http://savage-router.googlecode.com/files/TestActivityRotated.png" /> </p>