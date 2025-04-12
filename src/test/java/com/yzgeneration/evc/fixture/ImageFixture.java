package com.yzgeneration.evc.fixture;

import com.yzgeneration.evc.domain.image.dto.ImageRequest;

import java.util.List;

import static com.yzgeneration.evc.fixture.Fixture.fixtureMonkey;

public class ImageFixture {
    public static ImageRequest fixImageRequest() {
        return fixtureMonkey.giveMeBuilder(ImageRequest.class)
                .set("prefix", "prefix")
                .set("imageNames", List.of("imageName.jpg"))
                .sample();
    }
}
