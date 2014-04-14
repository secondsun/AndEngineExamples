package org.andengine.examples;

import android.widget.Toast;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import org.andengine.engine.options.EngineOptions;
import org.andengine.entity.primitive.Line;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.util.constants.PhysicsConstants;

/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 18:47:08 - 19.03.2010
 */
public class PhysicsRevoluteJointExample extends BasePhysicsJointExample {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public EngineOptions onCreateEngineOptions() {
		Toast.makeText(this, "In this example, the revolute joints have their motor enabled.", Toast.LENGTH_LONG).show();
		return super.onCreateEngineOptions();
	}

	@Override
	public Scene onCreateScene() {
		final Scene scene = super.onCreateScene();
		this.initJoints(scene);
		return scene;
	}

	// ===========================================================
	// Methods
	// ===========================================================

	private void initJoints(final Scene pScene) {
		final float centerX = CAMERA_WIDTH / 2;
		final float centerY = CAMERA_HEIGHT / 2;

		final FixtureDef objectFixtureDef = PhysicsFactory.createFixtureDef(10, 0.2f, 0.5f);

		for(int i = 0; i < 3; i++) {
			final float x = centerX + 220 * (i - 1);
			final AnimatedSprite anchorSprite = new AnimatedSprite(x, centerY, this.mBoxFaceTextureRegion, this.getVertexBufferObjectManager());
			anchorSprite.animate(200);
			final Body anchorBody = PhysicsFactory.createBoxBody(this.mPhysicsWorld, anchorSprite, BodyType.StaticBody, objectFixtureDef);

			final AnimatedSprite movingSprite = new AnimatedSprite(x, centerY + 90, this.mCircleFaceTextureRegion, this.getVertexBufferObjectManager());
			movingSprite.animate(200);
			final Body movingBody = PhysicsFactory.createCircleBody(this.mPhysicsWorld, movingSprite, BodyType.DynamicBody, objectFixtureDef);

			final Line connectionLine = new Line(x, centerY, x, centerY, this.getVertexBufferObjectManager());

			pScene.attachChild(connectionLine);
			pScene.attachChild(anchorSprite);
			pScene.attachChild(movingSprite);

			this.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(anchorSprite, anchorBody, true, true){
				@Override
				public void onUpdate(final float pSecondsElapsed) {
					super.onUpdate(pSecondsElapsed);
					final Vector2 movingBodyWorldCenter = movingBody.getWorldCenter();
					connectionLine.setPosition(connectionLine.getX1(), connectionLine.getY1(), movingBodyWorldCenter.x * PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT, movingBodyWorldCenter.y * PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT);
				}
			});
			this.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(movingSprite, movingBody, true, true));


			final RevoluteJointDef revoluteJointDef = new RevoluteJointDef();
			revoluteJointDef.initialize(anchorBody, movingBody, anchorBody.getWorldCenter());
			revoluteJointDef.enableMotor = true;
			revoluteJointDef.motorSpeed = 10;
			revoluteJointDef.maxMotorTorque = 200;

			this.mPhysicsWorld.createJoint(revoluteJointDef);
		}
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
