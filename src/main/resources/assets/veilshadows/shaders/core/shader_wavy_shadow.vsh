#version 150

in vec3 Position;
in vec2 UV0;
uniform mat4 ModelViewMat;
uniform mat4 ProjMat;
uniform float GameTime;
uniform vec3 CameraPos;

out vec2 v_UV0;
out float v_DistanceToCamera;

void main() {
    float distortionSpeed = 1000.0;
    float distortionAmount = 0.15;
    vec3 worldPos = (ModelViewMat * vec4(Position, 1.0)).xyz;
    float distanceFactor = distance(CameraPos, worldPos);
    float normalizedDistortion = distortionAmount / (1.0 + distanceFactor * 0.1);
    float waveX = sin(GameTime * distortionSpeed + Position.y * 2.0) * normalizedDistortion;
    float waveZ = cos(GameTime * distortionSpeed + Position.x * 2.0) * normalizedDistortion;
    float waveY = sin(GameTime * distortionSpeed * 0.5 + Position.z * 2.0) * normalizedDistortion;
    vec3 localPos = Position;
    localPos.x += waveX;
    localPos.z += waveZ;
    localPos.y += waveY * 0.5;
    vec4 worldPosition = ModelViewMat * vec4(localPos, 1.0);
    v_DistanceToCamera = distance(CameraPos, worldPos);
    gl_Position = ProjMat * worldPosition;
    v_UV0 = UV0;
}