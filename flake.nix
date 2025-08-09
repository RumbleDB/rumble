{
  description = "RumbleDB is a querying engine that allows you to query your large, messy datasets with ease and productivity.";

  inputs.nixpkgs.url = "github:nixos/nixpkgs?ref=nixos-unstable";

  outputs = inputs:
    let
      supportedSystems = [ "x86_64-linux" "aarch64-linux" "x86_64-darwin" "aarch64-darwin" ];
      forEachSupportedSystem = f: inputs.nixpkgs.lib.genAttrs supportedSystems (system: f {
        pkgs = import inputs.nixpkgs {
          inherit system;
          overlays = [ inputs.self.overlays.default ];
        };
      });
    in
    {
      overlays.default = final: prev:
        let
          jdk = prev."jdk17"; # java version to be used
        in
        {
          inherit jdk;
          maven = prev.maven.override { jdk_headless = jdk; };
        };
      devShells = forEachSupportedSystem ({ pkgs }: {
        default = pkgs.mkShell {
          packages = with pkgs; [
            jdk
            maven
            spark
            ant
          ];
        };
      });
    };
}
