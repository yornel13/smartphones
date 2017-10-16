package com.personal.yornel.androids.controller;

import android.content.Context;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.util.Log;

import com.google.gson.Gson;
import com.personal.yornel.androids.app.SearchActivity;
import com.personal.yornel.androids.event.LoadPhoneCompleteEvent;
import com.personal.yornel.androids.event.LoadPhoneFailureEvent;
import com.personal.yornel.androids.event.SearchPhoneCompleteEvent;
import com.personal.yornel.androids.event.SearchPhoneFailureEvent;
import com.personal.yornel.androids.model.Battery;
import com.personal.yornel.androids.model.Camera;
import com.personal.yornel.androids.model.Connectivity;
import com.personal.yornel.androids.model.GoogleResult;
import com.personal.yornel.androids.model.Memory;
import com.personal.yornel.androids.model.Phone;
import com.personal.yornel.androids.model.Processor;
import com.personal.yornel.androids.model.Screen;
import com.personal.yornel.androids.model.Smartphone;
import com.personal.yornel.androids.model.Version;
import com.squareup.otto.Bus;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.personal.yornel.androids.util.Const.GOOGLE_SEARCH_URL;
import static com.personal.yornel.androids.util.Const.PROVIDER_PAGE_URL;
import static com.personal.yornel.androids.util.Const.PROVIDER_PHONE_PART;
import static com.personal.yornel.androids.util.Const.USER_AGENT;

/**
 * Created by Yornel on 23/9/2017.
 */

public class SearchController extends BaseController {

    private SearchController(Context context, Bus bus) {
        super(context, bus);
    }

    public static SearchController createSearchController(Context context, Bus bus) {
        return new SearchController(context, bus);
    }

    public void search(String text) {
        text = text.replace(" ", "+");
        new GoogleSearchOperation(context, bus, text).execute();
    }

    public void loadPhone(String url) {
        new PhoneLoadOperation(context, bus, url).execute();
    }



    /**
     *  Connect to google search.
     */
    private class GoogleSearchOperation extends AsyncTask<String, Void, List<GoogleResult>> {

        private Context context;
        private Bus bus;
        private String text;

        public GoogleSearchOperation(Context context, Bus bus, String text) {
            this.context = context;
            this.bus = bus;
            this.text = text;
        }

        @Override
        protected List<GoogleResult> doInBackground(String... params) {

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            Document doc = null;
            try {
                String searchUrl = GOOGLE_SEARCH_URL+PROVIDER_PHONE_PART+text;
                System.out.println(searchUrl);
                doc = Jsoup.connect(searchUrl).userAgent(USER_AGENT).get();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }

            List<GoogleResult> results = new ArrayList<>();

            for (Element result : doc.select("h3.r a")){

                final String title = result.text();
                final String url = result.attr("href");

                if (url.contains(PROVIDER_PAGE_URL)) {

                    GoogleResult googleResult = new GoogleResult();
                    String shortTitle;
                    try {
                        shortTitle = title.split(":")[0];
                    } catch (Exception e) {
                        shortTitle = title;
                        Log.e(GoogleSearchOperation.class.getName(), "can't find simple name");
                    }
                    googleResult.setTitle(shortTitle);
                    googleResult.setUrl(url);
                    results.add(googleResult);
                }
            }
            return results;
        }

        @Override
        protected void onPostExecute(List<GoogleResult> results) {
            if (results == null) {
                bus.post(new SearchPhoneFailureEvent("Error"));
            } else {
                bus.post(new SearchPhoneCompleteEvent(results));
            }
        }
    }

    /**
     *  Connect to kimovil.
     */
    private class PhoneLoadOperation extends AsyncTask<String, Void, Smartphone> {

        private Context context;
        private Bus bus;
        private String url;

        public PhoneLoadOperation(Context context, Bus bus, String url) {
            this.context = context;
            this.bus = bus;
            this.url = url;
        }

        @Override
        protected Smartphone doInBackground(String... params) {

            Smartphone smartphone = new Smartphone();
            try {
                Document doc = Jsoup.connect(url).get();
                ////////////// Extract Title ///////////////////
                String name = doc.title();
                String shortTitle;
                try {
                    shortTitle = name.split(":")[0];
                } catch (Exception e) {
                    shortTitle = name;
                    Log.e(SearchActivity.class.getName(), "can't find simple name");
                }
                ///////////// Other versions ////////////////////
                List<Version> versions = new ArrayList<>();
                Elements others = doc.select("div.other-devices-list-version.tag-tabs");
                for (Element oth : others) {
                    for (Element other: oth.select("a[href]")) {
                        Version version = new Version();
                        version.setTitle(other.attr("title"));
                        version.setUrl(other.attr("href"));
                        version.setActive("false");
                        versions.add(version);
                    }
                    for (Element other: oth.select("li.item.active")) {
                        for (Version version: versions) {
                            String activeUrl = other.select("a[href]").get(0).attr("href").trim();
                            if (version.getUrl().trim().equalsIgnoreCase(activeUrl)) {
                                version.setActive("true");
                            }
                        }
                    }
                }
                ////////////// Extract images ///////////////////
                List<String> gallery = new ArrayList<>();
                String image = null;
                Elements links = doc.select("a[href]");
                for (Element link : links) {
                    String linkString =  link.attr("href");
                    String classString =  link.attr("class");
                    if (classString.equalsIgnoreCase("image kigallery")) {
                        gallery.add(linkString.replace("//", "http://"));
                    }
                    if (classString.equalsIgnoreCase("kigallery device-draggable")) {
                        image = linkString.replace("//", "http://");
                    }
                }
                ////////////// Extract content ///////////////////
                Screen screen = new Screen();
                Memory memory = new Memory();
                Processor processor = new Processor();
                Battery battery = new Battery();
                Camera camera = new Camera();
                Phone phone = new Phone();
                Connectivity connectivity = new Connectivity();

                for (Element element : doc.select("div.row")) {
                    String title = null;
                    for (Element ele : element.select("div.cell")) {
                        if (ele.attr("class").equalsIgnoreCase("cell left")) {
                            title = ele.text();
                        } else if (ele.attr("class").equalsIgnoreCase("cell right")) {
                            Elements size = ele.select("dt");
                            for (Element e: size) {
                                String subtitle = ele.select("dt").get(size.indexOf(e)).text();
                                Element contentElement;
                                try {
                                    contentElement = ele.select("dd").get(size.indexOf(e));
                                } catch (IndexOutOfBoundsException ex) {
                                    Log.e(GoogleSearchOperation.class.getName(), "can't find content");
                                    continue;
                                }

                                String content = contentElement.text();
                                content.trim();

                                Elements removeElement = contentElement.select("a[class]");
                                if (removeElement != null && !removeElement.isEmpty()) {
                                    String removeElementString = removeElement.get(0).text();
                                    try {
                                        String elementString = contentElement.text().replace(removeElementString, "");
                                        content = elementString.trim();
                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                    }
                                }

                                // Screen
                                if (subtitle.equalsIgnoreCase("Diagonal") && title.equalsIgnoreCase("Pantalla")) {
                                    content = content.replaceAll("[^\\d.]", "");
                                    screen.setSize(content);
                                } else if (subtitle.equalsIgnoreCase("Tipo") && title.equalsIgnoreCase("Pantalla")) {
                                    screen.setType(content);
                                } else if (subtitle.equalsIgnoreCase("Resolución") && title.equalsIgnoreCase("Pantalla")) {
                                    screen.setResolution(content);
                                } else if (subtitle.equalsIgnoreCase("Densidad") && title.equalsIgnoreCase("Pantalla")) {
                                    screen.setDensity(content);
                                } else if (subtitle.equalsIgnoreCase("Superficie Útil") && title.equalsIgnoreCase("Estructura")) {
                                    screen.setRatio(content);
                                }
                                // Memory
                                else if (subtitle.equalsIgnoreCase("RAM") && title.equalsIgnoreCase("RAM")) {
                                    memory.setRam(content);
                                } else if (subtitle.equalsIgnoreCase("Capacidad") && title.equalsIgnoreCase("Almacenamiento")) {
                                    memory.setRom(content);
                                } else if (subtitle.equalsIgnoreCase("¿Ampliable? (SD)") && title.equalsIgnoreCase("Almacenamiento")) {
                                    memory.setExpandable(content);
                                }
                                // Processor
                                else if (subtitle.equalsIgnoreCase("Modelo") && title.equalsIgnoreCase("Procesador")) {
                                    processor.setModel(content);
                                } else if (subtitle.equalsIgnoreCase("CPU") && title.equalsIgnoreCase("Procesador")) {
                                    processor.setCpu(content);
                                } else if (subtitle.equalsIgnoreCase("Tipo") && title.equalsIgnoreCase("Procesador")) {
                                    processor.setType(content);
                                } else if (subtitle.equalsIgnoreCase("Frecuencia de reloj") && title.equalsIgnoreCase("Procesador")) {
                                    processor.setFrequency(content);
                                } else if (subtitle.equalsIgnoreCase("64 Bits") && title.equalsIgnoreCase("Procesador")) {
                                    processor.setArchitecture(content);
                                } else if (subtitle.equalsIgnoreCase("GPU") && title.equalsIgnoreCase("Gráficos")) {
                                    processor.setGpu(content);
                                }
                                // Battery
                                else if (subtitle.equalsIgnoreCase("Capacidad") && title.equals("Batería")) {
                                    battery.setCapacity(content);
                                } else if (subtitle.equalsIgnoreCase("Tipo") && title.equals("Batería")) {
                                    battery.setType(content);
                                } else if (subtitle.equalsIgnoreCase("Otras") && title.equals("Batería")) {
                                    battery.setOthers(content);
                                }
                                // Camera
                                else if (subtitle.equalsIgnoreCase("Resolución") && title.equalsIgnoreCase("Principal")) {
                                    camera.setPrincipalResolution(content);
                                } else if (subtitle.equalsIgnoreCase("Sensor") && title.equalsIgnoreCase("Principal")) {
                                    camera.setPrincipalSensor(content);
                                } else if (subtitle.equalsIgnoreCase("Apertura") && title.equalsIgnoreCase("Principal")) {
                                    camera.setPrincipalAperture(content);
                                } else if (subtitle.equalsIgnoreCase("Flash") && title.equalsIgnoreCase("Principal")) {
                                    camera.setPrincipalFlash(content);
                                } else if (subtitle.equalsIgnoreCase("Resolución") && title.equalsIgnoreCase("Selfie")) {
                                    camera.setSecondaryResolution(content);
                                }
                                // Phone
                                else if (subtitle.equalsIgnoreCase("Marca") && title.equalsIgnoreCase("Marca")) {
                                    phone.setBrand(content);
                                } else if (subtitle.equalsIgnoreCase("Presentado") && title.equalsIgnoreCase("Presentación")) {
                                    phone.setLaunch(content);
                                } else if (subtitle.equalsIgnoreCase("Tamaño") && title.equalsIgnoreCase("Estructura")) {
                                    phone.setDimensions(content);
                                } else if (subtitle.equalsIgnoreCase("Peso") && title.equalsIgnoreCase("Estructura")) {
                                    phone.setWeight(content);
                                } else if (subtitle.equalsIgnoreCase("Materiales") && title.equalsIgnoreCase("Estructura")) {
                                    phone.setMaterial(content);
                                } else if (subtitle.equalsIgnoreCase("Colores") && title.equalsIgnoreCase("Estructura")) {
                                    phone.setColors(content);
                                } else if (subtitle.equalsIgnoreCase("Tipo") && title.equalsIgnoreCase("Tarjeta SIM")) {
                                    Elements rElement = contentElement.select("p[class]");
                                    if (rElement != null && !rElement.isEmpty()) {
                                        String removeElementString = rElement.get(0).text();
                                        try {
                                            content = content.replace(removeElementString, "");
                                        } catch (Exception ex) {
                                            ex.printStackTrace();
                                        }
                                    }
                                    phone.setSim(content.trim());
                                } else if (subtitle.equalsIgnoreCase("Audio Jack de 3,5mm") && title.equalsIgnoreCase("Otras")) {
                                    phone.setJack(content);
                                } else if (subtitle.equalsIgnoreCase("Sistema operativo") && title.equalsIgnoreCase("Software")) {
                                    Elements rElement = contentElement.select("small");
                                    if (rElement != null && !rElement.isEmpty()) {
                                        String removeElementString = rElement.get(0).text();
                                        try {
                                            content = content.replaceFirst(removeElementString, "");
                                            phone.setSystemUpdate(removeElementString.trim());
                                        } catch (Exception ex) {
                                            ex.printStackTrace();
                                        }
                                    }
                                    phone.setSystem(content.trim());
                                } else if (subtitle.equalsIgnoreCase("Huella dactilar") && title.equalsIgnoreCase("seguridad")) {
                                    phone.setFingerprint(content);
                                }
                                // Connectivity
                                else if (subtitle.equalsIgnoreCase("4G LTE") && title.equalsIgnoreCase("Redes")) {
                                    connectivity.setLte(content);
                                } else if (subtitle.equalsIgnoreCase("3G") && title.equalsIgnoreCase("Redes")) {
                                    connectivity.setHspa(content);
                                } else if (subtitle.equalsIgnoreCase("2G") && title.equalsIgnoreCase("Redes")) {
                                    connectivity.setEdge(content);
                                } else if (subtitle.equalsIgnoreCase("Versión") && title.equalsIgnoreCase("Bluetooth")) {
                                    connectivity.setBluetooth(content);
                                } else if (subtitle.equalsIgnoreCase("Estándares") && title.equalsIgnoreCase("Wifi")) {
                                    connectivity.setWifi(content);
                                } else if (subtitle.equalsIgnoreCase("Soporta") && title.equalsIgnoreCase("Navegación")) {
                                    connectivity.setNavigation(content);
                                }
                                // Antutu
                                else if (subtitle.equalsIgnoreCase("Puntuación") && title.equalsIgnoreCase("antutu")) {
                                    Elements rElement = contentElement.select("span");
                                    if (rElement != null && !rElement.isEmpty()) {
                                        String removeElementString = rElement.get(0).text();
                                        try {
                                            content = content.replace(removeElementString, "");
                                        } catch (Exception ex) {
                                            ex.printStackTrace();
                                        }
                                    }
                                    smartphone.setAntutu(content);
                                }
                            }
                        }
                    }
                }
                smartphone.setTitle(shortTitle);
                smartphone.setImage(image);
                smartphone.setGallery(gallery);
                smartphone.setScreen(screen);
                smartphone.setMemory(memory);
                smartphone.setProcessor(processor);
                smartphone.setBattery(battery);
                smartphone.setCamera(camera);
                smartphone.setPhone(phone);
                smartphone.setConnectivity(connectivity);
                smartphone.setVersions(versions);
                smartphone.setUrl(url);

            } catch (IOException e) {
                smartphone = null;
                e.printStackTrace();
            }
            return smartphone;
        }

        @Override
        protected void onPostExecute(Smartphone result) {
            if (result == null) {
                bus.post(new LoadPhoneFailureEvent("Error"));
            } else {
                bus.post(new LoadPhoneCompleteEvent(result));
            }
        }
    }
}
