package com.example.android.icummenical.Activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.android.icummenical.Classes.Permission;
import com.example.android.icummenical.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Dash;
import com.google.android.gms.maps.model.Dot;
import com.google.android.gms.maps.model.Gap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PatternItem;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.Polyline;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnPolylineClickListener,
        GoogleMap.OnPolygonClickListener,
        GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener,
        ActivityCompat.OnRequestPermissionsResultCallback {

    private static final int COLOR_BLACK_ARGB = 0xff000000;
    private static final int COLOR_WHITE_ARGB = 0xffffffff;
    private static final int COLOR_GREEN_ARGB = 0xff388E3C;
    private static final int COLOR_PURPLE_ARGB = 0xff81C784;
    private static final int COLOR_ORANGE_ARGB = 0xffF57F17;
    private static final int COLOR_BLUE_ARGB = 0xffF9A825;

    private static final int POLYGON_STROKE_WIDTH_PX = 8;
    private static final int PATTERN_DASH_LENGTH_PX = 20;
    private static final int PATTERN_GAP_LENGTH_PX = 20;
    private static final PatternItem DOT = new Dot();
    private static final PatternItem DASH = new Dash(PATTERN_DASH_LENGTH_PX);
    private static final PatternItem GAP = new Gap(PATTERN_GAP_LENGTH_PX);
    // Create a stroke pattern of a gap followed by a dot.
    // Cria um padrão de traçado de um intervalo seguido por um ponto.
    private static final List<PatternItem> PATTERN_POLYLINE_DOTTED = Arrays.asList(GAP, DOT);

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private boolean mPermissionDenied = false;

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        // Obtenha o SupportMapFragment e seja notificado quando o mapa estiver pronto para ser usado.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    // * Manipulates the map once available.
    //  * This callback is triggered when the map is ready to be used.
    // * This is where we can add markers or lines, add listeners or move the camera. In this case,
    // * we just add a marker near Sydney, Australia.
    // * If Google Play services is not installed on the device, the user will be prompted to install
    // * it inside the SupportMapFragment. This method will only be triggered once the user has
    // * installed Google Play services and returned to the app.
    //* Manipula o mapa uma vez disponível.
    //* Este retorno de chamada é acionado quando o mapa está pronto para ser usado.
    //* É aqui que podemos adicionar marcadores ou linhas, adicionar ouvintes ou mover a câmera. Nesse caso,
    // * nós apenas adicionamos um marcador perto de Sydney, Austrália.
    // * Se o Google Play Services não estiver instalado no dispositivo, o usuário será solicitado a instalar
    // * dentro do SupportMapFragment. Este método só será acionado quando o usuário tiver
    //* Instalei o Google Play Services e retornei ao aplicativo.

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setMapType(mMap.MAP_TYPE_NORMAL);
        mMap.getUiSettings().setZoomControlsEnabled(false);
        mMap.getUiSettings().setAllGesturesEnabled(true);
        mMap.getUiSettings().setMapToolbarEnabled(true);
        mMap.setOnMyLocationButtonClickListener(this);
        mMap.setOnMyLocationClickListener(this);
        enableMyLocation();
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng point) {

                // Criando MarkerOptions
                // Creating MarkerOptions
                MarkerOptions options = new MarkerOptions();

                // Definir a posição do marcador
                // Setting the position of the marker
                options.position(point);

                // Obtém LatLng do ponto tocado
                //Get LatLng from touched point
                double touchLat = point.latitude;
                double touchLong = point.longitude;

                // aqui está o GeoCoding reverso que ajuda na obtenção do endereço do latlng
                //here is reverse GeoCoding which helps in getting address from latlng

                try {
                    Geocoder geo = new Geocoder(MapsActivity.this.getApplicationContext(), Locale.getDefault());
                    List<Address> addresses = geo.getFromLocation(touchLat, touchLong, 1);
                    String address = "";
                    if (addresses.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "Waiting for Location", Toast.LENGTH_SHORT).show();
                    } else {

                        if (addresses.size() > 0) {
                            address = addresses.get(0).getFeatureName()
                                    + ", " + addresses.get(0).getLocality()
                                    + ", " + addresses.get(0).getAdminArea()
                                    + ", " + addresses.get(0).getCountryName();
                            Toast.makeText(getApplicationContext(), "Address:- " + address, Toast.LENGTH_LONG).show();
                        }
                        // desenha o marcador no local atualmente tocado
                        // draws the marker at the currently touched location
                        mMap.addMarker(new MarkerOptions().position(point).title(address));

                    }
                } catch (Exception e) {
                    // getFromLocation () às vezes pode falhar
                    // getFromLocation() may sometimes fail
                    e.printStackTrace();

                }
            }
        });
    }

    @Override
    public void onPolygonClick(Polygon polygon) {

    }

    @Override
    public void onPolylineClick(Polyline polyline) {

        // Virar do curso sólido para o padrão de traçado pontilhado.
        // Flip from solid stroke to dotted stroke pattern.
        if ((polyline.getPattern() == null) || (!polyline.getPattern().contains(DOT))) {
            polyline.setPattern(PATTERN_POLYLINE_DOTTED);
        } else {
            // O padrão é um traçado sólido.
            // The default pattern is a solid stroke.
            polyline.setPattern(null);
        }

        Toast.makeText(this, "Route type " + polyline.getTag().toString(),
                Toast.LENGTH_SHORT).show();
    }

    /**
     * Ativa a camada Meu local se a permissão de localização precisa tiver sido concedida.
     * Enables the My Location layer if the fine location permission has been granted.
     */
    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permissão para acessar o local está faltando.
            // Permission to access the location is missing.
            Permission.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
        } else if (mMap != null) {

            // O acesso ao local foi concedido ao aplicativo.
            // Access to the location has been granted to the app.
            mMap.setMyLocationEnabled(true);
        }
    }

    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT).show();

        // Retorna false para que não consumamos o evento e o comportamento padrão ainda ocorra
        // (a câmera anima a posição atual do usuário).
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
        Toast.makeText(this, "Current location:\n" + location, Toast.LENGTH_LONG).show();
        double lat = location.getLatitude();
        double lon = location.getLongitude();
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lon), 10));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return;
        }

        if (Permission.isPermissionGranted(permissions, grantResults,
                Manifest.permission.ACCESS_FINE_LOCATION)) {

            // Ativar a minha camada de localização, se a permissão foi concedida.
            // Enable the my location layer if the permission has been granted.
            enableMyLocation();
        } else {

            // Exibe a caixa de diálogo de erro de permissão ausente quando os fragmentos são retomados.
            // Display the missing permission error dialog when the fragments resume.
            mPermissionDenied = true;
        }
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        if (mPermissionDenied) {

            // Permissão não foi concedida, exibir diálogo de erro.
            // Permission was not granted, display error dialog.
            showMissingPermissionError();
            mPermissionDenied = false;
        }
    }

    /**
     * Exibe uma caixa de diálogo com uma mensagem de erro explicando que a permissão de localização está ausente.
     * Displays a dialog with error message explaining that the location permission is missing.
     */
    private void showMissingPermissionError() {
        Permission.PermissionDeniedDialog
                .newInstance(true).show(getSupportFragmentManager(), "dialog");
    }

}


