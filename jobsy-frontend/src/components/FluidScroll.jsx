/* eslint-disable react/no-unknown-property */
import { Canvas } from "@react-three/fiber";
import { ScrollControls, Scroll, Image, Preload, useScroll, Text } from "@react-three/drei";
import { useRef } from "react";
import { useFrame } from "@react-three/fiber";

function Images() {
  const group = useRef();
  const data = useScroll();

  useFrame(() => {
    if (!group.current) return;
    const imgs = group.current.children;

    // simple zoom distortion
    imgs.forEach((img, i) => {
      img.material.zoom = 1 + data.range(i * 0.2, 0.3) / 2;
    });
  });

 
}

function Typography() {
  return (
    <Text
      position={[0, 2.5, 0]}
      fontSize={0.6}
      color="white"
      anchorX="center"
      anchorY="middle"
      outlineWidth={0.05}
      outlineColor="black"
    >
      Jobsy
    </Text>
  );
}

export default function FluidScroll() {
  return (
    <Canvas camera={{ position: [0, 0, 5], fov: 45 }} gl={{ alpha: true }}>
      <ScrollControls damping={0.2} pages={3} distance={0.5}>
        <Scroll>
          <Typography />
          <Images />
        </Scroll>
        <Scroll html></Scroll>
        <Preload />
      </ScrollControls>
    </Canvas>
  );
}
